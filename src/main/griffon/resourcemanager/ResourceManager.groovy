/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package griffon.resourcemanager

import griffon.core.GriffonApplication
import groovy.beans.Bindable
import java.awt.Component
import java.awt.Container
import java.beans.PropertyChangeListener
import java.beans.PropertyEditor
import java.security.AccessController
import java.security.PrivilegedActionException
import java.security.PrivilegedExceptionAction
import org.slf4j.LoggerFactory
import org.springframework.beans.BeanWrapperImpl
import griffon.plugins.i18n.*

/**
 * @author Alexander Klein
 */
class ResourceManager implements Cloneable, ExtendedMessageSource, ConstrainedMessageSource {
    static final Map<Class, Class> WRAPPERS = [:]
    private static final BeanWrapperImpl registry = new BeanWrapperImpl(true)

    static {
        registry.useConfigValueEditors()
        WRAPPERS.put(byte.class, Byte.class);
        WRAPPERS.put(short.class, Short.class);
        WRAPPERS.put(char.class, Character.class);
        WRAPPERS.put(int.class, Integer.class);
        WRAPPERS.put(long.class, Long.class);
        WRAPPERS.put(float.class, Float.class);
        WRAPPERS.put(double.class, Double.class);
        WRAPPERS.put(boolean.class, Boolean.class);
    }

    @Bindable
    ObservableList customSuffixes = [] as ObservableList
    @Bindable
    String resourceSuffix = 'Resources'
    @Bindable
    ObservableList basenames = [] as ObservableList
    @Bindable
    Locale locale = Locale.default
    @Bindable
    ClassLoader loader = griffon.resourcemanager.ResourceManager.classLoader
    @Bindable
    String extension = 'groovy'
    @Bindable
    Class baseclass
    ObservableMap binding = [:] as ObservableMap
    org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass())

    private ConfigSlurper slurper = new ConfigSlurper()
    private ConfigObject config
    GriffonApplication app
    def builder

    private PropertyChangeListener configChanged = {evt ->
        config = null
    } as PropertyChangeListener

    ResourceManager(GriffonApplication app = null) {
        this.app = app
        binding.rm = this
        binding.app = app
        addPropertyChangeListener(configChanged)
        customSuffixes.addPropertyChangeListener(configChanged)
        basenames.addPropertyChangeListener(configChanged)
        binding.addPropertyChangeListener(configChanged)
    }

    @Override
    Object clone() {
        def newInstance = new ResourceManager(app: app, customSuffixes: customSuffixes, resourceSuffix: resourceSuffix, basenames: basenames,
                locale: locale, loader: loader, extension: extension, baseclass: baseclass, binding: binding, log: log)
        newInstance.binding.rm = newInstance
        newInstance
    }

    Object invokeMethod(String name, ConfigObject config = null, Object args) {
        return getObject(name, config, createMapFromArguments(args))
    }

    private Map createMapFromArguments(args) {
        Map data = [:]
        if (args instanceof Map) {
            data.putAll(args)
            return args
        }
        int idx = 0
        if (args instanceof Object[])
            args = args.toList()
        if (args instanceof Collection) {
            args.each { arg ->
                if (arg instanceof Map) {
                    data.putAll(arg)
                } else if (arg instanceof Collection || arg instanceof Object[]) {
                    arg.each { a ->
                        data.put("_$idx".toString(), a)
                        idx++
                    }
                } else {
                    data.put("_$idx".toString(), arg)
                    idx++
                }
            }
        } else if (args != null) {
            data.put('_0', args)
        }
        return data
    }

    Object getResource(String property) {
        getProperty(property)
    }

    Object getResource(String property, Object defaultValue) {
        def value = getProperty(property)
        if (value == null || (value instanceof ConfigObject && value.size() == 0))
            return defaultValue
        else
            return value
    }

    Object getResource(String property, Map data) {
        invokeMethod(property, data)
    }

    Object getResource(String property, Map data, Object defaultValue) {
        def value = invokeMethod(property, data)
        if (value == null || (value instanceof ConfigObject && value.size() == 0))
            return defaultValue
        else
            return value
    }

    Object getResource(String property, Collection data) {
        invokeMethod(property, createMapFromArguments(data))
    }

    Object getResource(String property, Collection data, Object defaultValue) {
        def value = invokeMethod(property, createMapFromArguments(data))
        if (value == null || (value instanceof ConfigObject && value.size() == 0))
            return defaultValue
        else
            return value
    }

    Object getResource(String property, Object[] data) {
        invokeMethod(property, createMapFromArguments(data))
    }

    Object getResource(String property, Object[] data, Object defaultValue) {
        def value = invokeMethod(property, createMapFromArguments(data))
        if (value == null || (value instanceof ConfigObject && value.size() == 0))
            return defaultValue
        else
            return value
    }

    protected String expandMessage(String key, Collection args, Object defaultMessage, Locale locale = null) throws NoSuchMessageException {
        expandMessage(key, createMapFromArguments(args), defaultMessage, locale)
    }

    protected String expandMessage(String key, Object[] args, Object defaultMessage, Locale locale = null) throws NoSuchMessageException {
        expandMessage(key, createMapFromArguments(args), defaultMessage, locale)
    }

    protected String expandMessage(String key, Map args, Object defaultMessage, Locale locale = null) throws NoSuchMessageException {
        def parts = key.split(/\./).iterator()
        ResourceManager rm = locale ? getAt(locale) : this
        def msg = rm.invokeMethod(parts.next(), args)
        while (!(msg instanceof String)) {
            if (msg == null) {
                if (defaultMessage == null)
                    throw new NoSuchMessageException(key, locale)
                msg = defaultMessage
            } else if (msg instanceof ConfigObject) {
                if (msg.size() == 0) {
                    if (defaultMessage == null)
                        throw new NoSuchMessageException(key, locale)
                    msg = defaultMessage
                } else if (parts.hasNext())
                    msg = getObject(parts.next(), msg, args)
                else {
                    if (defaultMessage == null)
                        throw new NoSuchMessageException(key, locale)
                    msg = defaultMessage
                }
            } else if (msg instanceof Closure) {
                switch (msg.maximumNumberOfParameters) {
                    case 0: msg = msg.call(); break
                    case 1: msg = msg.call(args); break
                    case 2: msg = msg.call(args, defaultMessage); break
                    case 3: msg = msg.call(args, defaultMessage, locale)
                }
            } else
                msg = msg.toString()
        }
        return msg
    }

    String getMessage(String key) throws NoSuchMessageException {
        expandMessage(key, (Map) null, null)
    }

    String getMessage(String key, String defaultMessage) {
        expandMessage(key, (Map) null, defaultMessage)
    }

    String getMessage(String key, Locale locale) throws NoSuchMessageException {
        expandMessage(key, (Map) null, locale)
    }

    String getMessage(String key, String defaultMessage, Locale locale) {
        expandMessage(key, (Map) null, defaultMessage, locale)
    }

    String getMessage(String key, List<?> args) throws NoSuchMessageException {
        expandMessage(key, args, null)
    }

    String getMessage(String key, List<?> args, String defaultMessage) {
        expandMessage(key, args, defaultMessage)
    }

    String getMessage(String key, List<?> args, Locale locale) throws NoSuchMessageException {
        expandMessage(key, args, locale)
    }

    String getMessage(String key, List<?> args, String defaultMessage, Locale locale) {
        expandMessage(key, args, defaultMessage, locale)
    }

    String getMessage(String key, Object[] args) throws NoSuchMessageException {
        expandMessage(key, args, null)
    }

    String getMessage(String key, Object[] args, String defaultMessage) {
        expandMessage(key, args, defaultMessage)
    }

    String getMessage(String key, Object[] args, String defaultMessage, Locale locale) {
        expandMessage(key, args, defaultMessage, locale)
    }

    String getMessage(String key, Object[] args, Locale locale) throws NoSuchMessageException {
        expandMessage(key, args, locale)
    }

    String getMessage(String key, Map args) throws NoSuchMessageException {
        expandMessage(key, args, null)
    }

    String getMessage(String key, Map args, String defaultMessage) {
        expandMessage(key, args, defaultMessage)
    }

    String getMessage(String key, Map args, String defaultMessage, Locale locale) {
        expandMessage(key, args, defaultMessage, locale)
    }

    String getMessage(String key, Map args, Locale locale) throws NoSuchMessageException {
        expandMessage(key, args, locale)
    }

    @Override
    Object getProperty(String property) {
        if (hasProperty(property))
            return this.getMetaClass().getProperty(this, property)
        return getObject(property)
    }

    Object getObject(String key, ConfigObject cfg = null, Map data = [:]) {
        if (!cfg)
            cfg = getConfig()
        if (!cfg)
            return null
        def object = cfg[key]
        if (object instanceof String && data)
            object = fillIn(object, data)
        def rm = this
        if (object instanceof ConfigObject) {
            object.getMetaClass().methodMissing = {name, args ->
                rm.invokeMethod(name, object, args)
            }
        }
        return object
    }

    String fillIn(String template, Map data) {
        def escaped = false
        def checkDigit = false
        def result = new StringBuilder()
        for (int p = 0; p < template.size(); p++) {
            char c = template.charAt(p)
            switch (c) {
                case '\\':
                    escaped = true
                    checkDigit = false
                    break
                case '#':
                    if (escaped) {
                        result << c
                        checkDigit = false
                    } else {
                        result << '$'
                        checkDigit = true
                    }
                    escaped = false
                    break
                case '0'..'9':
                    if (checkDigit)
                        result << '_'
                    result << c
                    escaped = false
                    checkDigit = false
                    break
                case '{':
                    if (escaped)
                        result << '\\'
                    result << c
                    escaped = false
                    checkDigit = false
                    break
                default:
                    result << c
                    escaped = false
                    checkDigit = false
            }
        }
        template = result.toString()
        template = (template =~ /([^\$\\])\{(\d+)\}/).replaceAll(/$1\$\{_$2\}/)
        template = (template =~ /^\{(\d+)\}/).replaceFirst(/\$\{_$1\}/)
        template = (template =~ /\\\{(\d+)\}/).replaceFirst(/\{$1\}/)
        def shell
        if (baseclass)
            shell = new GroovyShell(baseclass.classLoader, new Binding(data))
        else
            shell = new GroovyShell(new Binding(data))
        return shell.evaluate("\"$template\"")
    }

    ConfigObject getConfig() {
        if (!config) {
            ConfigObject temp
            basenames.reverse().each {name ->
                temp = processForName(name, '', temp, customSuffixes.reverse())
            }
            if (baseclass)
                temp = processForName("$baseclass.simpleName$resourceSuffix", baseclass.package.name, temp, customSuffixes.reverse())
            config = temp
        }
        return config
    }

    private def processForName(String name, String base, ConfigObject config, List customSuffixes) {
        if (base)
            base = "$base."
        getCandidateLocales().each { loc ->
            def cfg = getConfigObject("$base$name$loc")
            if (cfg)
                config = merge(config, cfg)
        }
        customSuffixes.each {suffix ->
            getCandidateLocales().each { loc ->
                def cfg = getConfigObject("$base$name$suffix$loc")
                if (cfg)
                    config = merge(config, cfg)
            }
        }
        return config
    }

    private ConfigObject merge(ConfigObject primary, ConfigObject secondary) {
        if (!primary) {
            primary = secondary
        } else {
            primary = primary.merge(secondary)
        }
        return primary
    }

    protected ConfigObject getConfigObject(String target) {
        // lazily fetching the first builder
        if (!builder) {
            builder = app?.builders?.getAt(0) ?: new ResourceBuilder()
            slurper.delegate = builder
        }
        // Load by class if it exists
        ConfigObject object
        if (baseclass) {
            try {
                Class cls
                cls = loader.loadClass(target)
                if (Script.isAssignableFrom(cls)) {
                    slurper.binding = binding
                    object = slurper.parse(cls)
                }
            } catch (ClassNotFoundException e) { }
        }
        String bundleName = target.replaceAll(/\./, '/')
        URL url
        // If not, load by .groovy
        if (!object) {
            url = loader.getResource("${bundleName}.$extension")
            slurper.binding = binding
            if (url) {
                object = slurper.parse(url)
            }
        }
        // Load .properties
        url = loader.getResource("${bundleName}.properties")
        if (url) {
            Properties prop = new Properties()
            InputStream is = openPrivilegedInputStream(url)
            if (is) {
                prop.load(is)
                is.close()
            }
            if (object)
                object = slurper.parse(prop).merge(object)
            else
                object = slurper.parse(prop)
        }
        return object
    }

    protected InputStream openPrivilegedInputStream(final URL url) throws IOException {
        InputStream stream = null;
        if (!url)
            return null
        try {
            stream = AccessController.doPrivileged(
                    new PrivilegedExceptionAction<InputStream>() {
                        public InputStream run() throws IOException {
                            URLConnection connection = url.openConnection()
                            if (connection != null) {
                                connection.useCaches = false
                                return connection.inputStream
                            }
                            return null
                        }
                    });
        } catch (PrivilegedActionException e) {
            throw (IOException) e.getException()
        }
        return stream
    }

    List<String> getCandidateLocales() {
        List<String> candidates = []
        candidates << ""
        if (locale.language)
            candidates << "_${locale.language}"
        if (locale.country)
            candidates << "_${locale.language}_${locale.country}"
        if (locale.variant)
            candidates << "_${locale.language}_${locale.country}_${locale.variant}"
        return candidates
    }

    def getAt(String locale) {
        def rm = clone()
        rm.locale = getLocaleFromString(locale)
        return rm
    }

    def getAt(Locale locale) {
        def rm = clone()
        rm.locale = locale ?: Locale.default
        return rm
    }

    def getAt(Class cls) {
        def rm = clone()
        rm.baseclass = cls
        return rm
    }

    def getAt(Object obj) {
        return getAt(obj.getClass())
    }

    static Locale getLocaleFromString(String localeString) {
        if (localeString == null)
            return Locale.default;
        localeString = localeString.trim();
        return new Locale(* localeString.split('_'))
    }

    /**
     * The first one in the list has the highest priority
     * @param customSuffixes
     */
    void setCustomSuffixes(Collection customSuffixes) {
        this.customSuffixes.clear()
        this.customSuffixes.addAll(customSuffixes)
    }

    /**
     * The first one in the list has the highest priority
     * @param basenames
     */
    void setBasenames(Collection basenames) {
        this.basenames.clear()
        this.basenames.addAll(basenames)
    }

    void inject(def bean, String prefix = 'injections') {
        Map base = getProperty(prefix)?.flatten([:]) ?: [:]
        base.each { key, value ->
            try {
                if (value instanceof Closure)
                    value = value.call()
                def obj = bean
                def parts = key.split(/\./)
                parts.eachWithIndex {part, idx ->
                    if (idx == parts.size() - 1) {
                        def type = obj?.getMetaClass()?.getMetaProperty(part)?.type
                        if (type) {
                            if (type.isPrimitive())
                                type = WRAPPERS[type]
                            if (!type.isAssignableFrom(value.getClass()))
                                value = registry.convertIfNecessary(value, type)
                        }
                        obj."$part" = value
                    } else if (obj instanceof Container) {
                        obj = componentFinder.call(obj, part)
                        if (obj == null)
                            throw new IllegalArgumentException("Component with name $part not found")
                    } else
                        obj = obj."$part"
                }
            } catch (e) {
                log.error("Error injecting key [$key] into [$bean]", e)
            }
        }
    }

    Closure componentFinder = { Container container, String name ->
        def iter = container.iterator()
        List<Container> children = []
        while (iter.hasNext() || !children.isEmpty()) {
            if (iter.hasNext()) {
                def component = iter.next()
                if (name == component.name)
                    return component
                if (component instanceof Container)
                    children.addAll(component.components)
            } else {
                List nextLevel = []
                for (Component child: children) {
                    if (name == child.name)
                        return child
                    if (child instanceof Container)
                        nextLevel.addAll(child.components)
                }
                iter = nextLevel.iterator()
                children = []
            }
        }
        return null
    }

    static void registerEditor(Class cls, PropertyEditor editor) {
        registry.registerSharedEditor(cls, editor)
    }

    MessageSource getMessageSource(Object baseclass) throws ConstraintNotSupportedException {
        if (!(baseclass instanceof Class))
            throw new ConstraintNotSupportedException(this, baseclass);
        return (MessageSource) getAt((Class) baseclass)
    }
}

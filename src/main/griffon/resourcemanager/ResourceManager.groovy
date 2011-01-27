package griffon.resourcemanager

import groovy.beans.Bindable
import java.beans.PropertyChangeListener
import java.security.AccessController
import java.security.PrivilegedActionException
import java.security.PrivilegedExceptionAction
import groovy.text.GStringTemplateEngine
import groovy.swing.SwingBuilder

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

/**
 * @author Alexander Klein
 */

class ResourceManager implements Cloneable {
    @Bindable
    ObservableList customSuffixes = [] as ObservableList
    @Bindable
    ObservableList basenames = [] as ObservableList
    @Bindable
    ObservableList basedirs = ['resources', 'i18n'] as ObservableList
    @Bindable
    Locale locale = Locale.default
    @Bindable
    ClassLoader loader = griffon.resourcemanager.ResourceManager.classLoader
    @Bindable
    String extension = 'groovy'
    @Bindable
    Class baseclass
    ObservableMap binding = [:] as ObservableMap

    private ConfigSlurper slurper = new ConfigSlurper()
    private ConfigObject config
    private GStringTemplateEngine engine

    private PropertyChangeListener configChanged = {evt ->
        config = null
    } as PropertyChangeListener

    ResourceManager() {
        addPropertyChangeListener(configChanged)
        customSuffixes.addPropertyChangeListener(configChanged)
        basenames.addPropertyChangeListener(configChanged)
        basedirs.addPropertyChangeListener(configChanged)
        binding.addPropertyChangeListener(configChanged)
    }

    @Override
    Object clone() {
        new ResourceManager(customSuffixes: customSuffixes, basenames: basenames, basedirs: basedirs,
                locale: locale, loader: loader, extension: extension, baseclass: baseclass, binding: binding)
    }

    @Override
    Object invokeMethod(String name, ConfigObject config = null, Object args) {
        Map data = [:]
        int idx = 0
        if (args instanceof Object[]) {
            args.each { arg ->
                if (arg instanceof Map) {
                    data.putAll(arg)
                } else if (arg instanceof Collection) {
                    arg.each { a ->
                        data.put("_$idx".toString(), a)
                        idx++
                    }
                } else {
                    data.put("_$idx".toString(), arg)
                    idx++
                }
            }
        } else {
            data.put('_0', args)
        }
        return getObject(name, config, data)
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
        if (object instanceof String && data) {
            def template = engine.createTemplate(object).make(data)
            object = template.toString()
        }
        def rm = this
        if (object instanceof ConfigObject) {
            object.getMetaClass().methodMissing = {name, args ->
                rm.invokeMethod(name, object, args)
            }
        }
        return object
    }

    ConfigObject getConfig() {
        if (!config) {
            ConfigObject temp
            def dirs = []
            dirs.addAll(basedirs)
            if (baseclass)
                dirs << "${baseclass.package.name}.resources"
            dirs.each { base ->
                def pfx = ''
                if(base == "${baseclass?.package?.name}.resources")
                    pfx = baseclass.name
                basenames.each {name ->
                    customSuffixes.each {suffix ->
                        getCandidateLocales().each { loc ->
                            def cfg = getConfigObject("$base.$name$suffix$loc", pfx)
                            if (cfg) {
                                temp = merge(temp, cfg)
                            }
                        }
                    }
                    getCandidateLocales().each { loc ->
                        def cfg = getConfigObject("$base.$name$loc", pfx)
                        if (cfg) {
                            temp = merge(temp, cfg)
                        }
                    }
                }
            }
            if (baseclass) {
/*
                ConfigObject cfg = getConfigObject("${baseclass.package.name}.resources.${baseclass.simpleName}", baseclass.name)
                if(cfg) {
                    temp = merge(cfg, temp)
                }
*/
                engine = new GStringTemplateEngine(baseclass.classLoader)
            } else {
                engine = new GStringTemplateEngine()
            }
            config = temp
        }
        return config
    }

    private ConfigObject merge(ConfigObject primary, ConfigObject secondary) {
        if (!primary) {
            primary = secondary
        } else {
            primary = secondary.merge(primary)
        }
        return primary
    }

    protected ConfigObject getConfigObject(String target, String prefix = null) {
        // Load by class if it exists
        ConfigObject object
        try {
            Class cls = baseclass.classLoader.loadClass(target)
            if (cls.isAssignableFrom(Script))
                object = this.prefix(slurper.parse(cls), prefix)
        } catch (ClassNotFoundException e) { }

        String bundleName = target.replaceAll(/\./, '/')
        URL url
        // If not, load by .groovy
        if (!object) {
            url = loader.getResource("${bundleName}.$extension")
            slurper.binding = binding
            if (url)
                object = this.prefix(slurper.parse(url), prefix)
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
                object = this.prefix(slurper.parse(prop), prefix).merge(object)
            else
                object = this.prefix(slurper.parse(prop), prefix)
        }
    }

    private ConfigObject prefix(ConfigObject cfg, String prefix) {
        if (!prefix)
            return cfg
        else {
            ConfigObject obj = cfg
            prefix.split(/\./).reverseEach {
                ConfigObject temp = new ConfigObject()
                temp.it = obj
                obj = temp
            }
            obj.configFile = cfg.configFile
            return obj
        }
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
        if (locale.variant)
            candidates << "_${locale.language}_${locale.country}_${locale.variant}"
        if (locale.country)
            candidates << "_${locale.language}_${locale.country}"
        if (locale.language)
            candidates << "_${locale.language}"
        candidates << ""
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

    static Locale getLocaleFromString(String localeString) {
        if (localeString == null)
            return Locale.default;
        localeString = localeString.trim();
        return new Locale(* localeString.split('_'))
    }

    void setCustomSuffixes(Collection customSuffixes) {
        this.customSuffixes.clear()
        this.customSuffixes.addAll(customSuffixes)
    }

    void setBasenames(Collection basenames) {
        this.basenames.clear()
        this.basenames.addAll(basenames)
    }

    void setBasedirs(Collection basedirs) {
        this.basedirs.clear()
        this.basedirs.addAll(basedirs)
    }

}
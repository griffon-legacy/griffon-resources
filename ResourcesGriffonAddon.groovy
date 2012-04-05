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

import griffon.core.GriffonApplication
import griffon.resourcemanager.*
import griffon.core.GriffonClass
import griffon.plugins.i18n.MessageSourceHolder
import java.awt.*
import javax.swing.Icon

/*
DefaultEditors:

all primitives
byte[]
char[]
java.nio.Charset
java.lang.Class
java.lang.Class[]
java.util.Currency
java.io.File
java.io.InputStream
org.xml.sax.InputSource
java.util.Locale
java.util.regexp.Pattern
java.util.Properties
java.lang.String[]
java.util.TimeZone
java.net.URI
java.net.URL
java.util.UUID
java.lang.Boolean
java.lang.Byte
java.lang.Character
java.lang.Short
java.lang.Integer
java.lang.Long
java.math.BigInteger
java.lang.Float
java.lang.Double
java.math.BigDecimal
java.util.Collection
java.util.Set
java.util.SortedSet
java.util.List
java.util.SortedMap
org.codehaus.groovy.runtime.GStringImpl
java.awt.Color
java.awt.Dimension
java.awt.Insets
java.awt.Point
java.awt.Rectangle
java.awt.Font
java.awt.Image
java.awt.Icon
java.awt.GradientPaint
java.awt.LinearGradientPaint
java.awt.RadialGradientPaint
java.awt.TexturePaint
*/

/**
 * @author Alexander Klein
 */
class ResourcesGriffonAddon {
    private ResourceManager resourceManager
    private static final String DEFAULT_I18N_FILE = 'messages'
    private static final String PROVIDER_NAME = 'resources'

    private GriffonApplication app

    // adds new factories to all builders
    def factories = ResourceBuilder.factoryMap

    def addonInit(app) {
        initResourceManager(app)
    }

    // adds application event handlers
    def events = [
            StartupStart: { app ->
                initResourceManager(app)
                app.mvcGroupManager.configurations.each {groupName, configuration ->
                    configuration.members.each { k, v ->
                        GriffonClass griffonClass = app.artifactManager.findGriffonClass(v)
                        Class cls = griffonClass?.clazz ?: Thread.currentThread().contextClassLoader.loadClass(v)
                        MetaClass metaClass = griffonClass?.getMetaClass() ?: cls.getMetaClass()
                        def rm = resourceManager[cls]
                        metaClass.rm = rm
                        metaClass.resourceManager = rm
                        if(MessageSourceHolder.instance.provider == PROVIDER_NAME) {
                            metaClass.getMessageSource = { -> rm }
                            metaClass.getI18n = { -> rm }
                            metaClass.getMessage = { Object... args -> rm.getMessage(* args) }
                        }
                    }
                }
            },
            LoadAddonEnd: { name, addon, app ->
                if (name == 'ResourcesGriffonAddon') {
                    ResourceManager.with {
                        registerEditor org.codehaus.groovy.runtime.GStringImpl, new ToStringEditor()
                        registerEditor Color, new ColorEditor()
                        registerEditor Dimension, new DimensionEditor()
                        registerEditor Insets, new InsetsEditor()
                        registerEditor Point, new PointEditor()
                        registerEditor Rectangle, new RectangleEditor()
                        registerEditor Font, new FontEditor()
                        registerEditor Image, new ImageEditor()
                        registerEditor Icon, new IconEditor()
                        registerEditor GradientPaint, new GradientPaintEditor()
                        registerEditor LinearGradientPaint, new LinearGradientPaintEditor()
                        registerEditor RadialGradientPaint, new RadialGradientPaintEditor()
                        registerEditor TexturePaint, new TexturePaintEditor()
                    }
                }
            }
    ]

    def initResourceManager(app) {
        if (resourceManager == null) {
            this.app = app
            def basenames = [DEFAULT_I18N_FILE] as LinkedHashSet
            def bn = app.config?.resources?.basenames
            if (bn instanceof Collection)
                basenames.addAll(bn)
            if (bn instanceof String)
                basenames << bn
            bn = app.config.i18n?.basenames
            if (bn instanceof Collection)
                basenames.addAll(bn)
            if (bn instanceof String)
                basenames << bn
            basenames = basenames.asList()
            resourceManager = new ResourceManager(app)
            resourceManager.basenames = basenames as ObservableList
            resourceManager.customSuffixes = (app.config?.resources?.customSuffixes ?: []) as ObservableList
            resourceManager.resourceSuffix = app.config?.resources?.resourceSuffix ?: 'Resources'
            resourceManager.locale = app.config?.resources?.locale ?: Locale.default
            resourceManager.loader = app.config?.resources?.loader ?: app.getClass().classLoader
            resourceManager.extension = app.config?.resources?.extension ?: 'groovy'
            resourceManager.log = app.log
            app.metaClass.resourceManager = resourceManager
            app.metaClass.rm = resourceManager
            // i18n-facade
            MessageSourceHolder.instance.registerMessageSource(PROVIDER_NAME, resourceManager)
        }
    }
}

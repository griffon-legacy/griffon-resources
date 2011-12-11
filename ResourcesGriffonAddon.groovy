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
import griffon.resourcemanager.ResourceBuilder
import griffon.resourcemanager.ResourceManager
import griffon.core.GriffonClass

/**
 * @author Alexander Klein
 */
class ResourcesGriffonAddon {
    private ResourceManager resourceManager
    private static final String DEFAULT_I18N_FILE = 'messages'

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
                app.config.mvcGroups.each { groupName, group ->
                    group.each { k, v ->
                        GriffonClass griffonClass = app.artifactManager.findGriffonClass(v)
                        Class cls = griffonClass?.clazz ?: loadClass(app, v)
                        MetaClass metaClass = griffonClass?.getMetaClass() ?: cls.getMetaClass()
                        def rm = resourceManager[cls]
                        metaClass.rm = rm
                        metaClass.resourceManager = rm
                    }
                }
            }
    ]

    def initResourceManager(app) {
        if (resourceManager == null) {
            def basenames = app.config?.resources?.basenames ?: [DEFAULT_I18N_FILE]
            if (!basenames.contains(DEFAULT_I18N_FILE))
                basenames = [DEFAULT_I18N_FILE] + basenames
            resourceManager = new ResourceManager(app)
            app.metaClass.resourceManager = resourceManager
            app.metaClass.rm = resourceManager
            resourceManager.basenames = basenames as ObservableList
            resourceManager.customSuffixes = (app.config?.resources?.customSuffixes ?: []) as ObservableList
            resourceManager.resourceSuffix = app.config?.resources?.resourceSuffix ?: 'Resources'
            resourceManager.locale = app.config?.resources?.locale ?: Locale.default
            resourceManager.loader = app.config?.resources?.loader ?: ResourceManager.classLoader
            resourceManager.extension = app.config?.resources?.extension ?: 'groovy'
            resourceManager.log = app.log
            this.app = app
        }
    }
}

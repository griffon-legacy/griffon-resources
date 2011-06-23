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
class ResourcesGriffonPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Griffon the plugin is designed for
    def griffonVersion = '0.9.2 > *'
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are included in plugin packaging
    def pluginIncludes = []
    // the plugin license
    def license = 'Apache Software License 2.0'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    def toolkits = []
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    def platforms = []

    def author = 'Alexander Klein'
    def authorEmail = 'info@aklein.org'
    def title = 'i18n and resource manager'
    def description = '''
Groovy-style i18n support for messages and resources like images and so on
'''

    // URL to the plugin's documentation
    def documentation = 'http://griffon.codehaus.org/Resources+Plugin'
}

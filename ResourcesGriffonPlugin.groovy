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
    def version = "0.3"
    // the version or versions of Griffon the plugin is designed for
    def griffonVersion = '0.9.5-rc2 > *'
    // the other plugins this plugin depends on
    def dependsOn = ['i18n-support': '0.1']
    // resources that are included in plugin packaging
    List pluginIncludes = []
    // the plugin license
    String license = 'Apache Software License 2.0'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    List toolkits = []
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    List platforms = []

    // URL where documentation can be found
    String documentation = 'http://griffon.codehaus.org/Resources+Plugin'
    // URL where source can be found
    String source = 'https://github.com/karfunkel/griffon-resources'

    List authors = [
            [
                    name: 'Alexander Klein',
                    email: 'info@aklein.org'
            ]
    ]
    def title = 'i18n and resource manager'
    // accepts Markdown syntax. See http://daringfireball.net/projects/markdown/ for details
    def description = '''
**resources** provides internationalization support in a groovy way.<br/>

This plugin provides an implementation of `i18n-support`, but goes beyond that, providing i18n for resources like images and so on aswell. <br/>

The used implementation can be configured with the configuration key `i18n.provider`. <br/>
The key for this provider is `resources`.

Usage
-----
This implementation does support ExtendedMessageSource, so args may not be a Map. <br/>
Aswell this implementation does support GString-like replacements. <br/>
For more info's see the documentation of i18n-support.

### MessageSource (ResourceManger) access
You can get hold of the MessageSource instance with `messageSource`, `i18n`, `resourceManager` or `rm`  on artifacts and the app-instance.
If `i18n.provider = 'resources'` all 4 properties point to the same instance. If e.g. `i18n.provider = 'i18n-support'` `messageSource` and `i18n` point to
i18n-support's implementation but `resourceManager` and `rm` always point to this plugin's implementation (ResourceManager). <br/>

*Example with `i18n.provider = 'resources'`*

messages.properties:

    key.static = This is just a text

Your Code:

    assert 'This is just a text' == rm.key.static
    assert 'This is just a text' == resourceManager.key.static
    assert 'This is just a text' == i18n.key.static
    assert 'This is just a text' == messageSource.key.static

### Resource formats
ResourceManager supports two formats for defining messages/resources (resource-files): Properties- and ConfigSlurper-files.<br/>
Properties-files have to end with .properties while the extension of a ConfigSlurper-file can be defined by the configuration key `resources.extension`.

ConfigSlurper-based files cannot only define Strings but any Object. <br/>
If getMessage is called and the resolved result is a Closure, it will be called with the following arguments:

- if three parameters are defined: `call(args, defaultMessage, locale)`
- if two parameters are defined, `call(args, defaultMessage)`
- if one parameter is defined, `call(args)`
- if no parameters are defined, `call()`

Only if the resolved value or the result of a Closure is of type String, the argument-replacement of `getMessage` takes place.

*Example*

messages.properties:

    key.static = This is just a text

messages.groovy:

    key {
        static = 'This is just a text'
        object = Locale.GERMAN
        closure = { args, defaultMessage, locale ->
            if(!args.value)
                return defaultMessage
            return NumberFormat.getInstance(locale).format(args.value)
        }
        closure2 = { 'The key #key has the value #value'.toLowerCase() }
    }

Your Code:

    assert 'This is just a text' == rm.key.static
    assert Locale.GERMAN == rm.key.object
    assert rm.key.closure instanceof Closure
    assert '---' == rm.getMessage('key.closure', [], '---', Locale.GERMAN)
    assert '1.000,123' = rm.getMessage('key.closure', [value: 1000.123], '---', Locale.GERMAN)
    assert 'the key X has the value 100' == rm.getMessage('key.closure2', [key: 'X', value: 100])

### Resource customization
You can define customized Resource-files, for e.g. to specify plattform- or application-logic specific resources. Each resource-file can be related to only
one custom-name, but the resolving mechanism can use multiple custom-names.

*Examples*

    messages_windows.groovy
    messages_windows_de_DE.groovy
    messages_linux.groovy
    messages_linux_de_DE.groovy
    messages.groovy
    messages_de_DE.groovy

    rm.customSuffixes = ['windows', 'linux']

### Resource-file naming
The naming of resource-files fo// URL where documentation can be found
    String documentation = 'http://griffon.codehaus.org/I18n+Support+Plugin'
    // URL where source can be found
    String source = 'https://github.com/griffon/griffon-i18n-support-plugin'llows the basic rules of ResourceBundle: <br/>
`<bundlename>_<customname>_<language>_<country>_<variant>.<extension>` while all but bundlename and extension is optional, along with it's underscores.<br/>
e.g.: messages_custom_de.properties

### Class-based resources
ResourceManager can aswell fetch class-specific resources. <br/>
If a baseclass is set for a ResourceManager-instance, it tries to find a resource-file in the same package as the baseclass named like the `baseclass+resourceSuffix`. <br/>
This works for precompiled GroovyScripts aswell. <br/>
e.g.: baseclass = my.pkg.MyClass -> my.pkg.MyClassResources_custom_de.groovy or my.pkg.MyClassResources.properties

For all artifacts, the ResourceManager-instance you receive has already set the artifacts class as baseclass.

### Resolving
The ResourceManager looks for resource-files in the classPath of the ClassLoader specified by `resources.loader`, that defaults to the app-instance's ClassLoader.<br/>
All .properties-files in griffon-app/i18n and griffon-app/resources will be coverted via native2ascii automatically.

The ResourceManager tries to find a key's value in the following order (top most has highest precedence):
1. <baseclass+suffix>_<customname>_<locale>.<extension>
2. <baseclass+suffix>_<customname>_<locale>.properties
3. <baseclass+suffix>_<locale>.<extension>
4. <baseclass+suffix>_<locale>.properties
5. <baseclass+suffix>.<extension>
6. <baseclass+suffix>.properties
7. <bundlename>_<customname>_<locale>.<extension>
8. <bundlename>_<customname>_<locale>.properties
9. <bundlename>_<locale>.<extension>
10. <bundlename>_<locale>.properties
11. <bundlename>.<extension>
12. <bundlename>.properties

If multiple bundlenames exist, 7. to 12. loop in the order of the bundlenames specified in `i18n.basenames` <br/>
If multiple customizations exist, 1. to 2. and 7. to 8. loop in the order of the customizations specified in `resources.customSuffixes` <br/>

### Resource access
Additional to the `i18n-support`'s `getMessage` function ResourceManager can be accessed with the following methods:

- `rm.getResource(String key, String defaultMessage = null, def args = null,)` where args can be of the types `Object[]`, `Collection<?>` and `Map<String, ?>` <br/>
  Similar to `getMessage` but delivers the pure Object. Only a String's argument-replacement takes place. Another difference is, that `getResource` only
  does not descend into child hirarchie.
- The more groovy way to access is via getProperty and invokeMethod: <br/>
  `rm.test.key` is the same as `getMessage('test.key')` without calling a Closure result<br/>
  `rm.test.key ?: 'abc'` is the same as `getMessage('test.key', 'abc')` without calling a Closure result<br/>
  `rm.test.key([1,2,3])` is the same as `getMessage('test.key', [1,2,3])` without calling a Closure result<br/>
  `rm.test.key([a:1, b:2]) ?: 'abc'` is the same as `getMessage('test.key', [a:1, b:2], 'abc')` without calling a Closure result
- To access a ResourceManager for a different locale, you can use <br/>
  `rm[Locale.GERMANY]` or `rm['de_DE']`
- To access a ResourceManager for a baseclass, you can use <br/>
  `rm[MyClass]` or `rm[instanceOfMyClass]`

### ResourceBuilder
In ConfigSlurper-based resource-files, all nodes from your UberBuilder can be accessed. This allows to specify i18nable codeparts or the like.<br/>
Additionally the ResourceBuild provided by this plugin helps defining often localized resources like images, fonts or colors. The following nodes exist (with examples):

- **url** <br/>
    - `url('url specification')`
    - `url(source: 'url specification')`
    - `url(url: 'url specification')`
    - `url(src: 'url specification')`
- **uri** <br/>
    - `uri('url specification')`
    - `uri(source: 'url specification')`
    - `uri(url: 'url specification')`
    - `uri(src: 'url specification')`
- **dimension** <br/>
    - `dimension('100, 200')`
    - `dimension([100, 200])`
    - `dimension(width: 100, height: 200)`
- **insets** <br/>
    - `insets([10, 20, 30, 40])`
    - `insets('10, 20, 30, 40')`
    - `insets(t: 10, l: 20, b: 30, r: 40)`
    - `insets(top: 10, left: 20, bottom: 30, right: 40)`
- **point** <br/>
    - `point('10, 20')`
    - `point([10, 20])`
    - `point(x: 10, y: 20)`
- **locale** <br/>
    - `locale('de_DE_BW')`
    - `locale(locale: 'de_DE_BW')`
- **rectangle** <br/>
    - `rectangle('10,20,30,40')`
    - `rectangle([10, 20, 30, 40])`
    - `rectangle(x: 10, y: 20, w: 30, h: 40)`
    - `rectangle(x: 10, y: 20, width: 30, height: 40)`
    - `rectangle(point: point(10, 20), dimension: dimension(30, 40))`
- **color** <br/>
    - `color('#4488bbff')`                                      // RGBA
    - `color('#4488bb')`                                        // RGB
    - `color('#48b')`                                           // RGB CSS notation
    - `color('#48bf')`                                          // RGBA CSS notation
    - `color('WHITE')`                                          // java.awt.Color-names
    - `color(0x44, 0x88, 0xBB, 0xFF)`                           // RGBA
    - `color(0x44, 0x88, 0xBB)`                                 // RGB
    - `color(r:0x44, g:0x88, b:0xBB, a:0xFF)`
    - `color(red:0x44, green:0x88, blue:0xBB, alpha:0xFF)`
- **font** <br/>
    - `font("Arial, 12")`
    - `font("'Times New Roman', 12, bold, italic, underline, strikethrough, [kerning: on, ligatures: on]")`
    - `font(['Times New Roman', 12, 'bold', 'italic', 'underline', 'strikethrough', [kerning: TextAttribute.KERNING_ON, ligatures: TextAttribute.LIGATURES_ON]])`
    - `font([FAMILY: 'Times New Roman', SIZE: 12, WEIGHT: TextAttribute.WEIGHT_BOLD, POSTURE: 'OBLIQUE'])`
- **image** <br/>
    - `image('testImage.png')`
    - `image(url('testImage.png'))`
    - `image(uri('testImage.png'))`
    - `image(MyClass.getResource('testImage.png').newInputStream())`
    - `image(MyClass.getResource('testImage.png').getBytes())`
    - `image(new File('test/unit/resourcemanager/testImage.png'))`
    - `image(source: 'testImage.png')`
    - `...`
    - `image(src: 'testImage.png')`
    - `...`
- **icon** <br/>
    - `icon('testImage.png')`
    - `icon(url('testImage.png'))`
    - `icon(uri('testImage.png'))`             f possible i
    - `icon(MyClass.getResource('testImage.png').newInputStream())`
    - `icon(MyClass.getResource('testImage.png').getBytes())`
    - `icon(new File('test/unit/resourcemanager/testImage.png'))`
    - `icon(source: 'testImage.png')`
    - `...`
    - `icon(src: 'testImage.png')`
    - `...`
- **gradientPaint** <br/>
    - `gradientPaint('10, 20, WHITE, 100, 200, #AA5500')`
    - `gradientPaint([10, 20, Color.WHITE, 100, 200, '#AA5500'])`
    - `gradientPaint(x1:10, y1:20, startColor:Color.WHITE, x2:100, y2:200, endColor:'#AA5500')`
    - `gradientPaint(x1:10, y1:20, color1:Color.WHITE, x2:100, y2:200, color2:'#AA5500')`
    - `gradientPaint(x1:10, y1:20, c1:Color.WHITE, x2:100, y2:200, c2:'#AA5500')`
- **linearGradientPaint** <br/>
    - `linearGradientPaint("10, 20, 100, 200, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK]")`
    - `linearGradientPaint("10, 20, 100, 200, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT")`
    - `linearGradientPaint([10, 20, 100, 200, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK]])`
    - `linearGradientPaint([10, 20, 100, 200, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], CycleMethod.REPEAT])`
    - `linearGradientPaint(x1:10, y1:20, x2:100, y2:200, colors: [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], cycleMethod: CycleMethod.REPEAT)`
    - `linearGradientPaint(x1:10, y1:20, x2:100, y2:200, cols: [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], cycle: CycleMethod.REPEAT)`
- **radialGradientPaint** <br/>
    - `radialGradientPaint("100, 200, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK]")`
    - `radialGradientPaint("100, 200, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT")`
    - `radialGradientPaint([100, 200, 50, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK]])`
    - `radialGradientPaint([100, 200, 50, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], CycleMethod.REPEAT])`
    - `radialGradientPaint(cx: 100, cy:200, r:50, cols:[0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], cycle:CycleMethod.REPEAT)`
    - `radialGradientPaint("100, 200, 10, 20, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK]")`
    - `radialGradientPaint("100, 200, 10, 20, 50, [0.0: WHITE, 0.5: #AAAAAA, 1.0: BLACK], REPEAT")`
    - `radialGradientPaint([100, 200, 10, 20, 50, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK]])`
    - `radialGradientPaint([100, 200, 10, 20, 50, [0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], CycleMethod.REPEAT])`
    - `radialGradientPaint(cx: 100, cy:200, fx:10, fy:20, radius:50, colors:[0.0: Color.WHITE, 0.5: '#AAAAAA', 1.0: Color.BLACK], cycleMethod:CycleMethod.REPEAT)`
- **texturePaint** <br/>
    - `texturePaint('testImage.png', 10, 20, 50, 100")`
    - `texturePaint(image('testImage.png'), [10, 20, 50, 100]])`
    - `texturePaint(['testImage.png', new Rectangle(10, 20, 50, 100)])`
    - `texturePaint([image('testImage.png'), rectangle(10, 20, 50, 100)])`
    - `texturePaint(image: image('testImage.png'), anchor: rectangle(10, 20, 50, 100))`
    - `texturePaint(source: image('testImage.png'), rectangle: rectangle(10, 20, 50, 100))`
    - `texturePaint(src: this.getClass().getResource('testImage.png'), rect: rectangle(10, 20, 50, 100))`

### Injection
ResourceManager provides the ability to inject data into any Object via getters and setter. <br/>
`rm.inject(Object bean, String prefix = 'injections')` takes all keys that reside in the key defined by `prefix` and iterates over its children.
If the bean has a setter to set the property with the name of the childrens key, it sets the value. <br/>
It tries to descend into the beans hirarchy to set subkeys. <br/>
If the bean is a java.awt.Component, it tries to identify subcomponents by its `name` property.

It tries to convert the value to the right type before setting it by using java.beans.PropertyEditor's. <br/>
By default, ResourceManager supports PropertyEditors for all types specified under ResourceBuilder (with the same Parameters) but custom
implementations can be registered via `ResourceManager.registerEditor(Class cls, PropertyEditor editor)`.

*Example:*

    // Bean-Class
    class A {
        Closure property1
        int property2
        B property3 = new B()
        URL url
    }

    class B {
        String sub1
    }

    // ConfigSlurper-based resource-file
    injections {
        property1 = { 'Test' }
        property2 = '2'
        property3.sub1 = 'Value3'
        url = 'http://www.google.de'
    }

    // Call of inject e.g. in a Controller
    def a = new A()
    rm.inject(a)
    assert a.property1 instanceof Closure
    assert a.property2 == 2
    assert a.property3.sub1 == 'Value3'
    assert a.url == new URL(http://www.google.de)


Configuration
-------------
    i18n.basenames = ['messages']                 // Bundlename for resource-files
    resources.customSuffixes = []                 // Bundlesuffixes for customizing files
    resources.resourceSuffix = 'Resources'        // Suffix to the classname for classspecific resources
    resources.locale = Locale.default             // The ResourceManagers default locale
    resources.loader = app.getClass().classLoader // The ClassLoader to load the resource-files from
    resources.extension = 'groovy'                // Extension for ConfigSlurper-based resource-files

'''
}

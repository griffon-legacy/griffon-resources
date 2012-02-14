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

package resourcemanager

import griffon.resourcemanager.ResourceManager
import griffon.test.GriffonUnitTestCase
import groovy.beans.Bindable
import java.awt.font.TextAttribute
import java.beans.PropertyChangeListener
import java.awt.*
import java.awt.MultipleGradientPaint.CycleMethod

/**
 * @author Alexander Klein
 */
class ResourceManagerTests extends GriffonUnitTestCase {
    @Bindable
    String property1
    @Bindable
    int property2
    ObservableMap property3 = [sub1: ''] as ObservableMap
    URL url1

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testBasics() {
        def rm = new ResourceManager()
        assert rm.locale == Locale.default
        assert rm.customSuffixes == []
        assert rm.basenames == []

        rm = new ResourceManager(locale: Locale.GERMANY, customSuffixes: ['custom'], basenames: ['messages'])
        assert rm.locale.toString() == 'de_DE'
        assert rm.customSuffixes == ['custom']
        assert rm.basenames == ['messages']
    }

    void testGetAt() {
        def rm = new ResourceManager()
        assert rm[Locale.GERMANY].locale.toString() == 'de_DE'
        assert rm['de'].locale.toString() == 'de'
        assert rm['de_DE'].locale.toString() == 'de_DE'
        assert rm['de_DE_xyz'].locale.toString() == 'de_DE_xyz'
        assert rm[String].baseclass == String
    }

    void testGetProperty() {
        def rm = new ResourceManager(basenames: ['messages'], customSuffixes: ['_custom'])
        rm.loader = new URLClassLoader([new File('test/resourceBase/resources').toURL(), new File('test/resourceBase').toURL()] as URL[], resourcemanager.ResourceManagerTests.getClassLoader())
        rm = rm[this.getClass()]
        assert rm.test.key1 == 'resourcemanager/resources/ResourceManagerTests_custom_de.groovy'
        assert rm.test.key2 == 'resourcemanager/resources/ResourceManagerTests_custom_de.properties'
        assert rm.test.key3 == 'resourcemanager/resources/ResourceManagerTests_custom.groovy'
        assert rm.test.key4 == 'resourcemanager/resources/ResourceManagerTests_custom.properties'
        assert rm.test.key5 == 'resourcemanager/resources/ResourceManagerTests_de.groovy'
        assert rm.test.key6 == 'resourcemanager/resources/ResourceManagerTests_de.properties'
        assert rm.test.key7 == 'resourcemanager/resources/ResourceManagerTests.groovy'
        assert rm.test.key8 == 'resourcemanager/resources/ResourceManagerTests.properties'
        assert rm.test.key9 == 'resources/messages_custom_de.groovy'
        assert rm.test.key10 == 'resources/messages_custom_de.properties'
        assert rm.test.key11 == 'resources/messages_custom.groovy'
        assert rm.test.key12 == 'resources/messages_custom.properties'
        assert rm.test.key13 == 'resources/messages_de.groovy'
        assert rm.test.key14 == 'resources/messages_de.properties'
        assert rm.test.key15 == 'resources/messages.groovy'
        assert rm.test.key16 == 'resources/messages.properties'

        assert rm.objects.boolean
        assert rm.objects.int == 123

        assert rm.objects.list == [1, 2, 3]
        assert rm.objects.map == [key: 'value']

        assert rm.key() == 'abc'
        assert rm.test.key16() == 'resources/messages.properties'
        assert rm.test.fill1(1, 2) == '[ 1, 2 ]'
        assert rm.test.fill1([1, 2]) == '[ 1, 2 ]'
        assert rm.test.fill2(a: 1, b: 2) == '[ 1, 2, #a ]'
        assert rm.test.fill2([a: 1, b: 2]) == '[ 1, 2, #a ]'
        assert rm.test.fill3(1) == '[ 1 ]'
        assert rm.test.fill4(1) == '1[ 1 {a} ]1'
        assert rm.test.fill5(1) == '{0}'

        assert rm.test.dynamic(1, 2) == '1 2'
        assert rm.test.dynamic(* [1, 2]) == '1 2'

        assert rm.callTest == 'Test'
    }

    void testPropertyEditorsProperty() {
        def rm = new ResourceManager(basenames: ['messages'], customSuffixes: ['_custom'])
        def url = new File('test/resourceBase').toURL()
        rm.loader = new URLClassLoader([url] as URL[], resourcemanager.ResourceManagerTests.getClassLoader())
        rm = rm[this.getClass()]

        assert rm.url1.toExternalForm() == new URL('http://www.google.de').toExternalForm()
        assert rm.url2.toExternalForm() == new URL('http://www.google.de').toExternalForm()
        assert rm.url3.toExternalForm() == new URL('http://www.google.de').toExternalForm()
        assert rm.url4.toExternalForm() == new URL('http://www.google.de').toExternalForm()

        assert rm.uri1.toASCIIString() == new URI('http://www.google.de').toASCIIString()
        assert rm.uri2.toASCIIString() == new URI('http://www.google.de').toASCIIString()
        assert rm.uri3.toASCIIString() == new URI('http://www.google.de').toASCIIString()
        assert rm.uri4.toASCIIString() == new URI('http://www.google.de').toASCIIString()

        assert rm.dimension1 == new Dimension(100, 200)
        assert rm.dimension2 == new Dimension(100, 200)
        assert rm.dimension3 == new Dimension(100, 200)
        assert rm.dimension4 == new Dimension(100, 200)

        assert rm.insets1 == new Insets(10, 20, 30, 40)
        assert rm.insets2 == new Insets(10, 20, 30, 40)
        assert rm.insets3 == new Insets(10, 20, 30, 40)
        assert rm.insets4 == new Insets(10, 20, 30, 40)
        assert rm.insets5 == new Insets(10, 20, 30, 40)
        assert rm.insets6 == new Insets(10, 20, 30, 40)

        assert rm.point1 == new Point(10, 20)
        assert rm.point2 == new Point(10, 20)
        assert rm.point3 == new Point(10, 20)
        assert rm.point4 == new Point(10, 20)

        assert rm.locale1 == new Locale('de', 'DE', 'BW')
        assert rm.locale2 == new Locale('de', 'DE', 'BW')

        assert rm.rectangle1 == new Rectangle(10, 20, 30, 40)
        assert rm.rectangle2 == new Rectangle(10, 20, 30, 40)
        assert rm.rectangle3 == new Rectangle(10, 20, 30, 40)
        assert rm.rectangle4 == new Rectangle(10, 20, 30, 40)
        assert rm.rectangle5 == new Rectangle(10, 20, 30, 40)
        assert rm.rectangle6 == new Rectangle(10, 20, 30, 40)

        assert rm.color1 == new Color(0x44, 0x88, 0xBB, 0xFF)
        assert rm.color2 == new Color(0x44, 0x88, 0xBB, 0xFF)
        assert rm.color3 == new Color(0x44, 0x88, 0xBB, 0xFF)
        assert rm.color4 == new Color(0x44, 0x88, 0xBB, 0xFF)
        assert rm.color5 == Color.WHITE
        assert rm.color6 == Color.BLACK
        assert rm.color7 == new Color(0x44, 0x88, 0xBB, 0xFF)
        assert rm.color8 == new Color(0x44, 0x88, 0xBB, 0xFF)
        assert rm.color9 == new Color(0x44, 0x88, 0xBB, 0xFF)
        assert rm.color10 == new Color(0x44, 0x88, 0xBB, 0xFF)

        assert rm.font1.attributes[TextAttribute.FAMILY] == 'Times New Roman'
        assert rm.font1.size == 12
        assert rm.font1.attributes[TextAttribute.WEIGHT] == TextAttribute.WEIGHT_BOLD
        assert rm.font1.attributes[TextAttribute.POSTURE] == TextAttribute.POSTURE_OBLIQUE
        assert rm.font1.attributes[TextAttribute.UNDERLINE] == TextAttribute.UNDERLINE_ON
        assert rm.font1.attributes[TextAttribute.STRIKETHROUGH] == TextAttribute.STRIKETHROUGH_ON
        assert rm.font1.attributes[TextAttribute.KERNING] == TextAttribute.KERNING_ON
        assert rm.font1.attributes[TextAttribute.LIGATURES] == TextAttribute.LIGATURES_ON

        assert rm.font2.attributes[TextAttribute.FAMILY] == 'Arial'
        assert rm.font2.size == 12

        assert rm.font3.attributes[TextAttribute.FAMILY] == 'Times New Roman'
        assert rm.font3.size == 12
        assert rm.font3.attributes[TextAttribute.WEIGHT] == TextAttribute.WEIGHT_BOLD
        assert rm.font3.attributes[TextAttribute.POSTURE] == TextAttribute.POSTURE_OBLIQUE
        assert rm.font3.attributes[TextAttribute.UNDERLINE] == TextAttribute.UNDERLINE_ON
        assert rm.font3.attributes[TextAttribute.STRIKETHROUGH] == TextAttribute.STRIKETHROUGH_ON
        assert rm.font3.attributes[TextAttribute.KERNING] == TextAttribute.KERNING_ON
        assert rm.font3.attributes[TextAttribute.LIGATURES] == TextAttribute.LIGATURES_ON

        assert rm.font4.attributes[TextAttribute.FAMILY] == 'Times New Roman'
        assert rm.font4.size == 12
        assert rm.font4.attributes[TextAttribute.WEIGHT] == TextAttribute.WEIGHT_BOLD
        assert rm.font4.attributes[TextAttribute.POSTURE] == TextAttribute.POSTURE_OBLIQUE

        assert rm.image1.width == 10
        assert rm.image1.height == 20
        assert rm.image2.width == 10
        assert rm.image2.height == 20
        assert rm.image3.width == 10
        assert rm.image3.height == 20
        assert rm.image4.width == 10
        assert rm.image4.height == 20
        assert rm.image5.width == 10
        assert rm.image5.height == 20
        assert rm.image6.width == 10
        assert rm.image6.height == 20
        assert rm.image7.width == 10
        assert rm.image7.height == 20
        assert rm.image8.width == 10
        assert rm.image8.height == 20

        assert rm.icon1.width == 10
        assert rm.icon1.height == 20
        assert rm.icon2.width == 10
        assert rm.icon2.height == 20
        assert rm.icon3.width == 10
        assert rm.icon3.height == 20
        assert rm.icon4.width == 10
        assert rm.icon4.height == 20
        assert rm.icon5.width == 10
        assert rm.icon5.height == 20
        assert rm.icon6.width == 10
        assert rm.icon6.height == 20
        assert rm.icon7.width == 10
        assert rm.icon7.height == 20
        assert rm.icon8.width == 10
        assert rm.icon8.height == 20

        assert rm.gradient1.point1 == new Point(10, 20)
        assert rm.gradient1.point2 == new Point(100, 200)
        assert rm.gradient1.color1 == Color.WHITE
        assert rm.gradient1.color2 == new Color(0xAA, 0x55, 0x00)
        assert rm.gradient2.point1 == new Point(10, 20)
        assert rm.gradient2.point2 == new Point(100, 200)
        assert rm.gradient2.color1 == Color.WHITE
        assert rm.gradient2.color2 == new Color(0xAA, 0x55, 0x00)
        assert rm.gradient3.point1 == new Point(10, 20)
        assert rm.gradient3.point2 == new Point(100, 200)
        assert rm.gradient3.color1 == Color.WHITE
        assert rm.gradient3.color2 == new Color(0xAA, 0x55, 0x00)
        assert rm.gradient4.point1 == new Point(10, 20)
        assert rm.gradient4.point2 == new Point(100, 200)
        assert rm.gradient4.color1 == Color.WHITE
        assert rm.gradient4.color2 == new Color(0xAA, 0x55, 0x00)
        assert rm.gradient5.point1 == new Point(10, 20)
        assert rm.gradient5.point2 == new Point(100, 200)
        assert rm.gradient5.color1 == Color.WHITE
        assert rm.gradient5.color2 == new Color(0xAA, 0x55, 0x00)

        assert rm.linear1.startPoint == new Point(10, 20)
        assert rm.linear1.endPoint == new Point(100, 200)
        assert rm.linear1.fractions == [0.0, 0.5, 1.0] as float[]
        assert rm.linear1.colors == [Color.WHITE, new Color(0xAA, 0xAA, 0xAA), Color.BLACK] as Color[]
        assert rm.linear1.cycleMethod == CycleMethod.REPEAT
        assert rm.linear2.startPoint == new Point(10, 20)
        assert rm.linear2.endPoint == new Point(100, 200)
        assert rm.linear2.fractions == [0.0, 0.5, 1.0] as float[]
        assert rm.linear2.colors == [Color.WHITE, new Color(0xAA, 0xAA, 0xAA), Color.BLACK] as Color[]
        assert rm.linear2.cycleMethod == CycleMethod.REPEAT
        assert rm.linear3.startPoint == new Point(10, 20)
        assert rm.linear3.endPoint == new Point(100, 200)
        assert rm.linear3.fractions == [0.0, 0.5, 1.0] as float[]
        assert rm.linear3.colors == [Color.WHITE, new Color(0xAA, 0xAA, 0xAA), Color.BLACK] as Color[]
        assert rm.linear3.cycleMethod == CycleMethod.REPEAT
        assert rm.linear4.startPoint == new Point(10, 20)
        assert rm.linear4.endPoint == new Point(100, 200)
        assert rm.linear4.fractions == [0.0, 0.5, 1.0] as float[]
        assert rm.linear4.colors == [Color.WHITE, new Color(0xAA, 0xAA, 0xAA), Color.BLACK] as Color[]
        assert rm.linear4.cycleMethod == CycleMethod.REPEAT
        assert rm.linear5.startPoint == new Point(10, 20)
        assert rm.linear5.endPoint == new Point(100, 200)
        assert rm.linear5.fractions == [0.0, 0.5, 1.0] as float[]
        assert rm.linear5.colors == [Color.WHITE, new Color(0xAA, 0xAA, 0xAA), Color.BLACK] as Color[]
        assert rm.linear5.cycleMethod == CycleMethod.REPEAT
        assert rm.linear6.startPoint == new Point(10, 20)
        assert rm.linear6.endPoint == new Point(100, 200)
        assert rm.linear6.fractions == [0.0, 0.5, 1.0] as float[]
        assert rm.linear6.colors == [Color.WHITE, new Color(0xAA, 0xAA, 0xAA), Color.BLACK] as Color[]
        assert rm.linear6.cycleMethod == CycleMethod.REPEAT

        assert rm.radial1.centerPoint == new Point(100, 200)
        assert rm.radial1.radius == 50
        assert rm.radial1.fractions == [0.0, 0.5, 1.0] as float[]
        assert rm.radial1.colors == [Color.WHITE, new Color(0xAA, 0xAA, 0xAA), Color.BLACK] as Color[]
        assert rm.radial1.cycleMethod == CycleMethod.REPEAT
        assert rm.radial2.centerPoint == new Point(100, 200)
        assert rm.radial2.radius == 50
        assert rm.radial2.fractions == [0.0, 0.5, 1.0] as float[]
        assert rm.radial2.colors == [Color.WHITE, new Color(0xAA, 0xAA, 0xAA), Color.BLACK] as Color[]
        assert rm.radial2.cycleMethod == CycleMethod.REPEAT
        assert rm.radial3.centerPoint == new Point(100, 200)
        assert rm.radial3.radius == 50
        assert rm.radial3.fractions == [0.0, 0.5, 1.0] as float[]
        assert rm.radial3.colors == [Color.WHITE, new Color(0xAA, 0xAA, 0xAA), Color.BLACK] as Color[]
        assert rm.radial3.cycleMethod == CycleMethod.REPEAT
        assert rm.radial4.centerPoint == new Point(100, 200)
        assert rm.radial4.radius == 50
        assert rm.radial4.fractions == [0.0, 0.5, 1.0] as float[]
        assert rm.radial4.colors == [Color.WHITE, new Color(0xAA, 0xAA, 0xAA), Color.BLACK] as Color[]
        assert rm.radial4.cycleMethod == CycleMethod.REPEAT
        assert rm.radial5.centerPoint == new Point(100, 200)
        assert rm.radial5.radius == 50
        assert rm.radial5.fractions == [0.0, 0.5, 1.0] as float[]
        assert rm.radial5.colors == [Color.WHITE, new Color(0xAA, 0xAA, 0xAA), Color.BLACK] as Color[]
        assert rm.radial5.cycleMethod == CycleMethod.REPEAT

        assert rm.radial6.centerPoint == new Point(100, 200)
        assert rm.radial6.focusPoint == new Point(10, 20)
        assert rm.radial6.radius == 50
        assert rm.radial6.fractions == [0.0, 0.5, 1.0] as float[]
        assert rm.radial6.colors == [Color.WHITE, new Color(0xAA, 0xAA, 0xAA), Color.BLACK] as Color[]
        assert rm.radial6.cycleMethod == CycleMethod.REPEAT
        assert rm.radial7.centerPoint == new Point(100, 200)
        assert rm.radial7.focusPoint == new Point(10, 20)
        assert rm.radial7.radius == 50
        assert rm.radial7.fractions == [0.0, 0.5, 1.0] as float[]
        assert rm.radial7.colors == [Color.WHITE, new Color(0xAA, 0xAA, 0xAA), Color.BLACK] as Color[]
        assert rm.radial7.cycleMethod == CycleMethod.REPEAT
        assert rm.radial8.centerPoint == new Point(100, 200)
        assert rm.radial8.focusPoint == new Point(10, 20)
        assert rm.radial8.radius == 50
        assert rm.radial8.fractions == [0.0, 0.5, 1.0] as float[]
        assert rm.radial8.colors == [Color.WHITE, new Color(0xAA, 0xAA, 0xAA), Color.BLACK] as Color[]
        assert rm.radial8.cycleMethod == CycleMethod.REPEAT
        assert rm.radial9.centerPoint == new Point(100, 200)
        assert rm.radial9.focusPoint == new Point(10, 20)
        assert rm.radial9.radius == 50
        assert rm.radial9.fractions == [0.0, 0.5, 1.0] as float[]
        assert rm.radial9.colors == [Color.WHITE, new Color(0xAA, 0xAA, 0xAA), Color.BLACK] as Color[]
        assert rm.radial9.cycleMethod == CycleMethod.REPEAT
        assert rm.radial10.centerPoint == new Point(100, 200)
        assert rm.radial10.focusPoint == new Point(10, 20)
        assert rm.radial10.radius == 50
        assert rm.radial10.fractions == [0.0, 0.5, 1.0] as float[]
        assert rm.radial10.colors == [Color.WHITE, new Color(0xAA, 0xAA, 0xAA), Color.BLACK] as Color[]
        assert rm.radial10.cycleMethod == CycleMethod.REPEAT

        assert rm.texture1.image.width == 10
        assert rm.texture1.image.height == 20
        assert rm.texture1.anchorRect == new Rectangle(10, 20, 50, 100)
        assert rm.texture2.image.width == 10
        assert rm.texture2.image.height == 20
        assert rm.texture2.anchorRect == new Rectangle(10, 20, 50, 100)
        assert rm.texture3.image.width == 10
        assert rm.texture3.image.height == 20
        assert rm.texture3.anchorRect == new Rectangle(10, 20, 50, 100)
        assert rm.texture4.image.width == 10
        assert rm.texture4.image.height == 20
        assert rm.texture4.anchorRect == new Rectangle(10, 20, 50, 100)
        assert rm.texture5.image.width == 10
        assert rm.texture5.image.height == 20
        assert rm.texture5.anchorRect == new Rectangle(10, 20, 50, 100)
        assert rm.texture6.image.width == 10
        assert rm.texture6.image.height == 20
        assert rm.texture6.anchorRect == new Rectangle(10, 20, 50, 100)
        assert rm.texture6.image.width == 10
        assert rm.texture6.image.height == 20
        assert rm.texture6.anchorRect == new Rectangle(10, 20, 50, 100)
    }

    void testInjectProperty() {
        def property1
        def property2
        def property3
        def property3_key
        this.addPropertyChangeListener('property1', { evt ->
            property1 = evt.newValue
        } as PropertyChangeListener)

        this.addPropertyChangeListener('property2', { evt ->
            property2 = evt.newValue
        } as PropertyChangeListener)

        this.property3.addPropertyChangeListener({ evt ->
            property3 = evt.newValue
            property3_key = evt.propertyName
        } as PropertyChangeListener)

        def rm = new ResourceManager(basenames: ['messages'], customSuffixes: ['_custom'])
        def url = new File('test/resourceBase').toURL()
        rm.loader = new URLClassLoader([url] as URL[], resourcemanager.ResourceManagerTests.getClassLoader())
        rm = rm[this]
        rm.inject(this)

        assert property1 == 'Value1'
        assert property2 == 2
        assert property3 == 'Value3'
        assert property3_key == 'sub1'
        assert url1 == new URL('http://www.google.de')

        def map = [property3: [:]]
        rm.inject(map)

        assert map.property1 == 'Value1'
        assert map.property2 == '2' // No conversion b/c no target type
        assert map.property3.sub1 == 'Value3'
    }

    void testGetMessage() {
        ResourceManager rm = new ResourceManager(basenames: ['messages'], customSuffixes: ['_custom'])
        def url = new File('test/resourceBase').toURL()
        rm.loader = new URLClassLoader([url] as URL[], resourcemanager.ResourceManagerTests.getClassLoader())
        rm = rm[this][Locale.ENGLISH]
        
        assert rm.getMessage('i18n.string.one') == 'Testvalue'
        assert rm.getMessage('i18n.strin.one', 'Blah') == 'Blah'
        assert rm.getMessage('i18n.string.one', 'Blah') == 'Testvalue'
        assert rm.getMessage('i18n.string.one', '', Locale.GERMAN) == 'Testwert'
        assert rm.getMessage('i18n.string.two', ['Blubb'], '', Locale.GERMAN) == 'Testwert-Blubb'
        assert rm.getMessage('i18n.string.two', ['Blubb'], '') == 'Testvalue-Blubb'

        assert rm.getMessage('i18n.closure.one', ['Blah', 'Blubb'], 'x', Locale.GERMAN) == 'Testvalue'
        assert rm.getMessage('i18n.closure.two', ['Blah', 'Blubb'], 'x', Locale.GERMAN) == 'Testvalue 2'
        assert rm.getMessage('i18n.closure.three', ['Blah', 'Blubb'], 'x', Locale.GERMAN) == 'Testvalue 2 x'
        assert rm.getMessage('i18n.closure.four', ['Blah', 'Blubb'], 'x', Locale.GERMAN) == 'Testvalue 2 x de'
    }
}

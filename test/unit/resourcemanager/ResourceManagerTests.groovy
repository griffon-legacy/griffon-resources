package resourcemanager

import griffon.test.GriffonUnitTestCase
import griffon.resourcemanager.ResourceManager
import groovy.beans.Bindable
import java.beans.PropertyChangeListener
import java.awt.Dimension

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

        rm = new ResourceManager(locale: Locale.GERMANY, customSuffixes: ['custom'], basenames: ['messages'],
                basedirs: ['resources', 'i18n'])
        assert rm.locale.toString() == 'de_DE'
        assert rm.customSuffixes == ['custom']
        assert rm.basenames == ['messages']
        assert rm.basedirs == ['resources', 'i18n']
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
        def url = new File('test/resourceBase').toURL()
        rm.loader = new URLClassLoader([url] as URL[], resourcemanager.ResourceManagerTests.getClassLoader())
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
        assert rm.test.key17 == 'i18n/messages_custom_de.groovy'
        assert rm.test.key18 == 'i18n/messages_custom_de.properties'
        assert rm.test.key19 == 'i18n/messages_custom.groovy'
        assert rm.test.key20 == 'i18n/messages_custom.properties'
        assert rm.test.key21 == 'i18n/messages_de.groovy'
        assert rm.test.key22 == 'i18n/messages_de.properties'
        assert rm.test.key23 == 'i18n/messages.groovy'
        assert rm.test.key24 == 'i18n/messages.properties'

        assert rm.objects.boolean
        assert rm.objects.int == 123
        assert rm.objects.list == [1, 2, 3]
        assert rm.objects.map == [key: 'value']

        assert rm.key() == 'abc'
        assert rm.test.key24() == 'i18n/messages.properties'
        assert rm.test.fill1(1, 2) == '[ 1, 2 ]'
        assert rm.test.fill1([1, 2]) == '[ 1, 2 ]'
        assert rm.test.fill2(a: 1, b: 2) == '[ 1, 2, #a ]'
        assert rm.test.fill2([a: 1, b: 2]) == '[ 1, 2, #a ]'
        assert rm.test.fill3(1) == '[ 1 ]'

        assert rm.test.dynamic(1,2) == '1 2'
        assert rm.test.dynamic(*[1, 2]) == '1 2'

        assert rm.dimension1 == new Dimension(100, 200)
        assert rm.dimension2 == new Dimension(100, 200)
        assert rm.dimension3 == new Dimension(100, 200)
        assert rm.dimension4 == new Dimension(100, 200)
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
        rm.injectProperties(this)

        assert property1 == 'Value1'
        assert property2 == 2
        assert property3 == 'Value3'
        assert property3_key == 'sub1'
        assert url1 == new URL('http://www.google.de')

        def map = [property3: [:]]
        rm.injectProperties(map, this.getClass())

        assert map.property1 == 'Value1'
        assert map.property2 == '2' // No conversion b/c no target type
        assert map.property3.sub1 == 'Value3'
    }
}

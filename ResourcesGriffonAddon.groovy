import griffon.core.GriffonApplication
import griffon.resourcemanager.*

class ResourcesGriffonAddon {
    private final ResourceManager resourceManager = new ResourceManager()
    private static final String DEFAULT_I18N_FILE = 'messages'

    private GriffonApplication app
    private def builder

    // lifecycle methods

    // called once, after the addon is created

    def addonInit(app) {
        def basenames = app.config?.resources?.basenames ?: [DEFAULT_I18N_FILE]
        if (!basenames.contains(DEFAULT_I18N_FILE))
            basenames = [DEFAULT_I18N_FILE] + basenames
        app.metaClass.resourceManager = resourceManager
        app.metaClass.rsc = resourceManager
        resourceManager.basenames = basenames as ObservableList
        resourceManager.customSuffixes = (app.config?.resources?.customSuffixes ?: []) as ObservableList
        resourceManager.basedirs = (app.config?.resources?.basedirs ?: ['resources', 'i18n']) as ObservableList
        resourceManager.locale = app.config?.resources?.locale ?: Locale.default
        resourceManager.loader = app.config?.resources?.loader ?: ResourceManager.classLoader
        resourceManager.extension = app.config?.resources?.extension ?: 'groovy'
    }

    // called once, after all addons have been inited
    //def addonPostInit(app) {
    //}

    // called many times, after creating a builder

    def addonBuilderInit(app, builder) {
        this.app = app
        this.builder = builder
        resourceManager.binding.app = app
        resourceManager.binding.builder = builder
    }

    // called many times, after creating a builder and after
    // all addons have been inited
    //def addonBuilderPostInit(app) {
    //}

    // to add MVC Groups use create-mvc

    // builder fields, these are added to all builders.
    // closures can either be literal { it -> println it}
    // or they can be method closures: this.&method

    // adds methods to all builders
    //def methods = [
    //    methodName: { /*Closure*/ }
    //]

    // adds properties to all builders
    //def props = [
    //    propertyName: [
    //        get: { /* optional getter closure */ },
    //        set: {val-> /* optional setter closure */ },
    //  ]
    //]

    // adds new factories to all builders
    def factories = [
            url: new URLFactory(),
            uri: new URIFactory(),
            dimension: new DimensionFactory(),
            insets: new InsetsFactory(),
            point: new PointFactory(),
            locale: new LocaleFactory(),
            rectangle: new RectangleFactory()
    ]

    // adds application event handlers
    def events = [
            NewInstance: {Class cls, String type, Object instance ->
                def rm = resourceManager[cls]
                rm.binding.app = app
                rm.binding.builder = builder
                instance.metaClass.rsc = rm
            }
            //    "StartupStart": {app -> /* event hadler code */ }
    ]

    // handle synthetic node properties or
    // intercept existing ones
    //def attributeDelegates = [
    //    {builder, node, attributes -> /*handler code*/ }
    //]

    // called before a node is instantiated
    //def preInstantiateDelegates = [
    //    {builder, attributes, value -> /*handler code*/ }
    //]

    // called after the node was instantiated
    //def postInstantiateDelegates = [
    //    {builder, attributes, node -> /*handler code*/ }
    //]

    // called after the node has been fully
    // processed, including child content
    //def postNodeCompletionDelegates = [
    //    {builder, parent, node -> /*handler code*/ }
    //]
}

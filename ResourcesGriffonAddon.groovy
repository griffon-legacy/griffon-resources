import griffon.core.GriffonApplication
import org.springframework.beans.propertyeditors.LocaleEditor
import org.springframework.beans.propertyeditors.URIEditor
import org.springframework.beans.propertyeditors.URLEditor
import griffon.resourcemanager.*

class ResourcesGriffonAddon {
    private ResourceManager resourceManager
    private static final String DEFAULT_I18N_FILE = 'messages'

    private GriffonApplication app

    // lifecycle methods

    // called once, after the addon is created

    def addonInit(app) {
        def basenames = app.config?.resources?.basenames ?: [DEFAULT_I18N_FILE]
        if (!basenames.contains(DEFAULT_I18N_FILE))
            basenames = [DEFAULT_I18N_FILE] + basenames
        resourceManager = new ResourceManager(app)
        app.metaClass.resourceManager = resourceManager
        app.metaClass.rm = resourceManager
        resourceManager.basenames = basenames as ObservableList
        resourceManager.customSuffixes = (app.config?.resources?.customSuffixes ?: []) as ObservableList
        resourceManager.basedirs = (app.config?.resources?.basedirs ?: ['resources', 'i18n']) as ObservableList
        resourceManager.locale = app.config?.resources?.locale ?: Locale.default
        resourceManager.loader = app.config?.resources?.loader ?: ResourceManager.classLoader
        resourceManager.extension = app.config?.resources?.extension ?: 'groovy'
        resourceManager.log = app.log
        this.app = app
    }

    // called once, after all addons have been inited
    //def addonPostInit(app) {
    //}

    // called many times, after creating a builder
    //def addonBuilderInit(app, builder) {
    //}

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
    //def factories = [
    //]

    // adds application event handlers
    def events = [
            NewInstance: {Class cls, String type, Object instance ->
                def rm = resourceManager[cls]
                rm.binding.app = app
                try {
                    instance.setRm(rm)
                } catch (MissingMethodException mme) {
                    try {
                        instance.rm = rm
                    } catch (MissingPropertyException mpe) {
                        instance.metaClass.rm = rm
                    }
                }
                try {
                    instance.setResourceManager(rm)
                } catch (MissingMethodException mme) {
                    try {
                        instance.resourceManager = rm
                    } catch (MissingPropertyException mpe) {
                        instance.metaClass.resourceManager = rm
                    }
                }
            }
            //    "StartupStart": {app -> /* event handler code */ }
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

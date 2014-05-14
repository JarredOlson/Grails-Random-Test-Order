class RandomTestOrderGrailsPlugin {
    // the plugin version
    def version = "0.3"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.7 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "test"
    ]

    def author = "Jarred Olson (Twitter: @JarredOlson)"
    def authorEmail = ""
    def title = "Random Test Order - Allows you to run your tests in a random order"
    def description = '''
        Allows you to run your unit, integration, and functional tests in a random order.
        This is helpful when trying to identify tests that rely on data or setup from another test.

        {code}grails random-test-order [test type] [rerun]{code}

        [test type]
            -unit
            -integration
            -functional
        [rerun]
            -rerun

        examples:
            grails random-test-order -unit             //will run all unit tests in a random order
            grails random-test-order -unit -rerun      //will rerun all unit tests in the previous random order


        When you execute the script you will see output like the following:

        About to execute the following test app script...
        grails test-app -unit UnitBTests UnitCTests UnitATests

        This will allow you to copy/paste the outputted command in order to re-run your tests in the same order
        that they previously occurred on another machine.
    '''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/random-test-order"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}

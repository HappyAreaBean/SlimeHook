plugins {
    java
    id("de.eldoria.plugin-yml.paper")
}

paper {
    apiVersion = "1.19"

    main = "cc.happyareabean.swmhook.SWMHook"

    author = Constants.AUTHOR
    description = property("description").toString()
    website = Constants.WEBSITE

    generateLibrariesJson = false
    foliaSupported = false

    serverDependencies {

    }
}
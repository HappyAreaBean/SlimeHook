plugins {
    java
    id("de.eldoria.plugin-yml.bukkit")
}

bukkit {
    apiVersion = "1.13"

    main = "cc.happyareabean.swmhook.SWMHook"

    author = Constants.AUTHOR
    description = property("description").toString()
    website = Constants.WEBSITE

    generateLibrariesJson = false
    foliaSupported = false

    softDepend = listOf("SlimeWorldManager", "SwoftyWorldManager", "SlimeWorldPlugin")
}
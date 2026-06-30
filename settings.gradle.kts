rootProject.name = "SlimeHook"
include("aswm")
include("plugin")
include("core")
include("slimeworldplugin")

pluginManagement {
    plugins {
        id("net.kyori.indra.git") version "4.0.0"
    }
}
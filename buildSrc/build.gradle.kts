plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("net.kyori.indra.git:net.kyori.indra.git.gradle.plugin:4.0.0")
    implementation("de.eldoria.plugin-yml.paper:de.eldoria.plugin-yml.paper.gradle.plugin:0.9.0")
    implementation("de.eldoria.plugin-yml.bukkit:de.eldoria.plugin-yml.bukkit.gradle.plugin:0.9.0")
    implementation("com.gradleup.shadow:com.gradleup.shadow.gradle.plugin:9.4.3")
}
plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("net.kyori.indra.git:net.kyori.indra.git.gradle.plugin:4.0.0")
}
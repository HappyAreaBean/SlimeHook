plugins {
    java
    `maven-publish`
    id("com.gradleup.shadow") version "9.3.2"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("io.freefair.lombok") version "8.4"
    id("net.kyori.indra.git")
}

val versionOnly = property("version").toString()
val versionWithGit: String = versionOnly + "-${getGitHash()}"

allprojects {
    plugins.apply("io.freefair.lombok")

    group = "cc.happyareabean"
    version = versionWithGit
    description = "SlimeHook is a paper plugin that utilize SlimeWorldManager/AdvancedSlimePaper for generating template world as an arena for several plugins. "

    repositories {
        mavenCentral()

        maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
        maven { url = uri("https://repo.fantasyrealms.net/other-snapshots") }
        maven { url = uri("https://repo.fantasyrealms.net/other-libraries") }
        maven { url = uri("https://repo.fantasyrealms.net/releases") }
        maven { url = uri("https://repo.codemc.io/repository/nms/") }
        maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
        maven { url = uri("https://repo.glaremasters.me/repository/concuncan/") }
        maven { url = uri("https://repo.glaremasters.me/repository/public/") }
        maven { url = uri("https://repo.maven.apache.org/maven2/") }
        maven { url = uri("https://jitpack.io") }

        mavenLocal()
    }
}

dependencies {
    implementation(project("aswm"))
    implementation(project("plugin"))
    implementation(project("core"))
    implementation(project("slimeworldplugin"))
}

//val mcVersion = "1.8.8"
//val runDirectoryDir: String by if (project.hasProperty("runPaper.runDirectory")) {
//    property<String>("runPaper.runDirectory")
//} else {
//    "${project.rootProject.projectDir}/.run"
//}
//val runServerJar: String? by if (project.hasProperty("runPaper.serverJar")) {
//    property<String>("runPaper.serverJar")
//} else {
//    null
//}
//
//tasks.runServer {
//    minecraftVersion(mcVersion)
//    if (runServerJar != null) {
//        serverJar(runDirectory.file(runServerJar as String))
//    }
//    runDirectory.set(file(runDirectoryDir))
//    jvmArgs("-javaagent:slimeworldmanager-classmodifier-2.2.1.jar")
//}

tasks.processResources {
    expand("pluginVersion" to project.version, "commit" to getGitHash(), "buildDate" to getDate())
}

val relocatePackage = "cc.happyareabean.swmhook.libs"

tasks.shadowJar {
    archiveClassifier.set("")
    archiveVersion.set("")

    relocate("de.exlll.configlib", "${relocatePackage}.configlib")
    relocate("net.kyori", "${relocatePackage}.kyori")
    relocate("org.bstats", "${relocatePackage}.bstats")
    relocate("org.semver4j", "${relocatePackage}.semver4j")
    relocate("org.yaml", "${relocatePackage}.yaml")
    relocate("revxrsal.commands", "${relocatePackage}.commands")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    withJavadocJar()
}

tasks.compileJava {
    options.compilerArgs.addAll(listOf("-parameters"))
    options.encoding = "UTF-8"
}

tasks.javadoc {
    options.encoding = "UTF-8"
}

publishing {
    repositories {
        maven {
            name = "frsReleases"
            url = uri("https://repo.fantasyrealms.net/releases/")
            credentials {
                username = findProperty("frsRepositoryUsername").toString()
                password = findProperty("frsRepositoryPassword").toString()
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["shadow"])

            version = versionOnly
        }
    }
}

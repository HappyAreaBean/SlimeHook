import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    `maven-publish`
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("io.freefair.lombok") version "9.5.0"
    id("com.gradleup.shadow")
    id("net.kyori.indra.git")
    id("plugin-yml-bukkit")
    id("plugin-yml-paper")
}

val group = property("group")
val versionOnly = property("version").toString()
val versionWithGit: String = "${versionOnly}-${getGitSHashShort()}"

allprojects {
    plugins.apply("io.freefair.lombok")

    group = group
    version = versionOnly

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

val mcVersion = "1.8.8"

tasks.runServer {
    val runVersion = System.getenv("SERV_VERSION") ?: mcVersion
    val runDirectoryDir = System.getenv("SERV_RUN_DIR") ?: ".run"
    val runServerJar: String? = System.getenv("SERV_JAR")
    val runJvmArgs: String? = System.getenv("SERV_JVM_ARGS")

    dependsOn(shadowDevJar)
    pluginJars(shadowDevJar.get().archiveFile)

    minecraftVersion(runVersion)

    runServerJar?.let { serverJar(runDirectory.file(it)) }
    runJvmArgs?.let { jvmArgs(it) }

    runDirectory.set(rootProject.file(runDirectoryDir))
}

tasks.processResources {
    expand("pluginVersion" to versionOnly, "commit" to getGitHash(), "buildDate" to getBuildCreated())
}

val shadowDevJar = tasks.register<ShadowJar>("shadowDevJar") {
    description = "Create a unrelocated JAR for run-paper"

    from(sourceSets.main.map { it.output })
    configurations = project.configurations.runtimeClasspath.map { listOf(it) }

    archiveClassifier = "dev"
    archiveBaseName.set(rootProject.name)
}

val relocatePackage = "cc.happyareabean.swmhook.libs"

tasks.shadowJar {
    archiveClassifier.set("")
    archiveVersion.set("")

    commonRelocate("de.exlll.configlib")
    commonRelocate("org.bstats")
    commonRelocate("org.semver4j")
    commonRelocate("org.yaml")
    commonRelocate("revxrsal.commands")
    commonRelocate("com.infernalsuite.asp.loaders")
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

tasks.register("printGitHash") {
    description = "Print git hash commit"

    val gitHash = providers.provider { getGitHash() }
    doLast {
        println(gitHash.get())
    }
}

tasks.register("printVersion") {
    description = "Print version without commit hash"

    val version = providers.gradleProperty("version")
    doLast {
        println(version.get())
    }
}

tasks.register("printFullVersion") {
    description = "Print version with commit hash"

    val versionGit = providers.provider { versionWithGit }
    doLast {
        println(versionGit.get())
    }
}
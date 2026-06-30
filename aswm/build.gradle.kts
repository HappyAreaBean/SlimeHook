plugins {
    java
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.infernalsuite.com/repository/maven-snapshots/") }
}

val mcVersion = "1.20.4"

dependencies {
    compileOnly(project(":core"))

    compileOnly("io.papermc.paper:paper-api:$mcVersion-R0.1-SNAPSHOT")
    compileOnly("com.infernalsuite.aswm:api:$mcVersion-R0.1-SNAPSHOT")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

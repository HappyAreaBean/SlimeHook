plugins {
    java
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.infernalsuite.com/repository/maven-snapshots/") }
}

val mcVersion = "1.21.11"

dependencies {
    compileOnly(project(":core"))

    compileOnly("io.papermc.paper:paper-api:$mcVersion-R0.1-SNAPSHOT")

    compileOnly("com.infernalsuite.asp:api:4.0.0-SNAPSHOT")
    implementation("com.infernalsuite.asp:file-loader:4.0.0-SNAPSHOT")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

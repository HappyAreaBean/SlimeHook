plugins {
    java
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.github.HappyAreaBean.ConfigLib:configlib-core:2.3.1")

    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

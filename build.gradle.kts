plugins {
    id("java")
}

group = "org.wumoe.mc"
version = "0.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
}

tasks {
    jar {
        archiveBaseName.set("WuMoeMC")
    }
}

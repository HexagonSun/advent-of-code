plugins {
    `java-library`
    kotlin("jvm") version "1.7.21"
    id("org.openjfx.javafxplugin") version "0.0.13"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

javafx {
    modules("javafx.graphics")
}

dependencies {
    api("com.google.code.gson:gson:2.10")

    implementation("junit:junit:4.13.2")
    implementation("org.hamcrest:hamcrest-library:2.2")
}

tasks {
    named<Test>("test") {
        testLogging.showExceptions = true
    }
}

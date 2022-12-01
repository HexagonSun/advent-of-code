plugins {
    `java-library`
    kotlin("jvm") version "1.7.21"
    id("org.openjfx.javafxplugin") version "0.0.10"
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
    api("com.google.code.gson:gson:2.5")

    implementation("junit:junit:4.12")
    implementation("org.hamcrest:hamcrest-library:1.3")
}

tasks {
    named<Test>("test") {
        testLogging.showExceptions = true
    }
}

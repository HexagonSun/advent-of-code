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
    val junitVersion = "5.9.0"
    val assertjVersion = "3.23.1"

    api("com.google.code.gson:gson:2.10")

    testImplementation("org.hamcrest:hamcrest-library:2.2")
    testImplementation("org.assertj:assertj-core:$assertjVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")
    testImplementation("org.junit.platform:junit-platform-launcher:1.9.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging.showExceptions = true
    }
}

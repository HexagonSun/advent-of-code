plugins {
    `java-library`
    id("org.openjfx.javafxplugin") version "0.0.10"
}

repositories {
    mavenCentral()
}

sourceSets {
    main {
        java.srcDir("src/main/java")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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

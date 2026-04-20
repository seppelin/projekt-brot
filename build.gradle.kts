plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

application {
    mainClass.set("de.developergang.Main")
    applicationDefaultJvmArgs = listOf(
        "--enable-native-access=javafx.graphics",
    )
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "25"
    modules("javafx.controls")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.github.almasb:fxgl:25")
}

tasks.test {
    useJUnitPlatform()
}
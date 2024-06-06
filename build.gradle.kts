
plugins {
    java
    application
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.serialization") version "1.9.24"
}

group = "com.tobiask"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("one.microproject.rpi:rpi-drivers:2.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.java-websocket:Java-WebSocket:1.5.6")
    implementation("com.pi4j:pi4j-plugin-linuxfs:2.3.0")
    implementation("com.pi4j:pi4j-ktx:2.4.0") // Kotlin DSL
    implementation("com.pi4j:pi4j-core:2.3.0")
    implementation("com.pi4j:pi4j-plugin-raspberrypi:2.3.0")
    implementation("com.pi4j:pi4j-plugin-pigpio:2.3.0")
    implementation("org.slf4j:slf4j-simple:2.0.13")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "com.tobiask.MainKt"
}

tasks {
   /*val fatJar = register<Jar>("fatJar") {
        dependsOn.addAll(listOf("compileJava", "compileKotlin", "processResources")) // We need this for Gradle optimization to work
        archiveClassifier.set("standalone") // Naming the jar
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        manifest { attributes["Main-Class"] = "com.tobiask.MainKt"} // Provided we set it up in the application plugin configuration
        val sourcesMain = sourceSets.main.get()
        val contents = configurations.runtimeClasspath.get()
            .map { if (it.isDirectory) it else zipTree(it)  } +
                sourcesMain.output
       println(sourcesMain)
        from(contents)
    }

    build {
        dependsOn(fatJar) // Trigger fat jar creation during build
    }*/
}

kotlin {
    jvmToolchain(17)
}
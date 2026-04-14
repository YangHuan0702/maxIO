import com.sun.tools.javac.resources.compiler

plugins {
    id("java")
}

group = "com.halosky"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }

}

tasks.compileJava {
    options.compilerArgs.addAll(listOf("--add-modules", "jdk.incubator.vector"))
}

tasks.test{
    jvmArgs = listOf("--add-modules", "jdk.incubator.vector")
}


repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
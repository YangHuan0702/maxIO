plugins {
    id("java")
    id("org.springframework.boot") version ("3.2.4")
    id("io.spring.dependency-management") version ("1.1.4")
    id("org.graalvm.buildtools.native") version("0.10.1")
}

group = "com.halosky"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.projectlombok:lombok:1.18.44")

}

tasks.test {
    useJUnitPlatform()
}
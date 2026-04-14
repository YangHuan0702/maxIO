plugins {
    id("java")
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
    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("ch.qos.logback:logback-classic:1.5.32")

    implementation(project(":max-core"))
    implementation("com.alibaba.nacos:nacos-client:3.1.1")

}


tasks.test {
    useJUnitPlatform()
}
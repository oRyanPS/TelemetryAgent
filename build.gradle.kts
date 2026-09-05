plugins {
    id("java")
    id("application")
}

group = "br.com.oryanps.agent"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.oshi:oshi-core:7.6.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.19.2")
    implementation("org.slf4j:slf4j-simple:2.0.17")
    compileOnly("org.projectlombok:lombok:1.18.48")
    annotationProcessor("org.projectlombok:lombok:1.18.48")


    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testCompileOnly("org.projectlombok:lombok:1.18.48")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.48")
}

application {
    mainClass = "br.com.oryanps.agent.TelemetryAgentApp"
}

tasks.test {
    useJUnitPlatform()
}
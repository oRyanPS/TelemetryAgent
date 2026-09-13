plugins {
    id("java")
    id("application")
    id("com.gradleup.shadow") version "8.3.6"
}

group = "br.com.oryanps.agent"
version = "1.0"

repositories {
    mavenCentral()
}

val lombokVersion = "1.18.36"

dependencies {
    implementation("com.github.oshi:oshi-core:7.0.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("org.slf4j:slf4j-simple:2.0.16")

    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testCompileOnly("org.projectlombok:lombok:$lombokVersion")
    testAnnotationProcessor("org.projectlombok:lombok:$lombokVersion")
}

application {
    mainClass = "br.com.oryanps.agent.TelemetryAgentApp"
}

tasks.test {
    useJUnitPlatform()
}
plugins {
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    java
}

repositories {
    mavenCentral()
}

val versions = mapOf(
    "springShell" to "3.3.2",
    "mockito" to "5.5.0",
    "junitBom" to "5.10.0",
    "assertj" to "3.23.1",
    "lombok" to "1.18.30"
)

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.shell:spring-shell-starter:${versions["springShell"]}")

    compileOnly("org.projectlombok:lombok:${versions["lombok"]}")
    annotationProcessor("org.projectlombok:lombok:${versions["lombok"]}")


    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core:${versions["mockito"]}")
    testImplementation(platform("org.junit:junit-bom:${versions["junitBom"]}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:${versions["assertj"]}")
}

tasks.test {
    useJUnitPlatform()
}

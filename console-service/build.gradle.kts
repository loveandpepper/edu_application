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
    "lombok" to "1.18.30"
)

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.shell:spring-shell-starter:${versions["springShell"]}")

    compileOnly("org.projectlombok:lombok:${versions["lombok"]}")
    annotationProcessor("org.projectlombok:lombok:${versions["lombok"]}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

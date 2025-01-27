plugins {
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
    java
}

repositories {
    mavenCentral()
}

val versions = mapOf(
    "telegram" to "6.9.7.1",
    "lombok" to "1.18.30"
)

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.telegram:telegrambots:${versions["telegram"]}")

    compileOnly("org.projectlombok:lombok:${versions["lombok"]}")
    annotationProcessor("org.projectlombok:lombok:${versions["lombok"]}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

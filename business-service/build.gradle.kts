plugins {
    id("java")
    id("org.springframework.boot") version "3.4.1"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "org.hofftech"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
    }
}

val versions = mapOf(
    "jackson" to "2.15.2",
    "lombok" to "1.18.30",
    "logback" to "1.4.12",
    "slf4j" to "2.0.9",
    "junitBom" to "5.10.0",
    "assertj" to "3.23.1",
    "mockito" to "5.5.0",
    "springShell" to "3.3.2",
    "flyway" to "11.2.0",
    "flywayPostgresql" to "11.2.0",
    "mapStruct" to "1.6.3",
    "caffeine" to "3.2.0",
    "openApi" to "2.8.3",
    "cloud-stream" to "4.2.0",
    "testcontainers" to "1.20.4",
    "spring-test-cont" to "3.4.1"

)

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:${versions["jackson"]}")
    implementation("ch.qos.logback:logback-classic:${versions["logback"]}")
    implementation("org.slf4j:slf4j-api:${versions["slf4j"]}")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core:${versions["flyway"]}")
    implementation("org.mapstruct:mapstruct:${versions["mapStruct"]}")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${versions["openApi"]}")
    implementation("org.postgresql:postgresql")
    implementation("org.springframework.cloud:spring-cloud-stream:${versions["cloud-stream"]}")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")
    implementation("com.github.ben-manes.caffeine:caffeine:${versions["caffeine"]}")

    runtimeOnly("org.flywaydb:flyway-database-postgresql:${versions["flywayPostgresql"]}")
    compileOnly("org.projectlombok:lombok:${versions["lombok"]}")

    annotationProcessor("org.projectlombok:lombok:${versions["lombok"]}")
    annotationProcessor("org.mapstruct:mapstruct-processor:${versions["mapStruct"]}")

    testCompileOnly("org.projectlombok:lombok:${versions["lombok"]}")
    testAnnotationProcessor("org.projectlombok:lombok:${versions["lombok"]}")
    testImplementation(platform("org.junit:junit-bom:${versions["junitBom"]}"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.assertj:assertj-core:${versions["assertj"]}")
    testImplementation("org.mockito:mockito-core:${versions["mockito"]}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers:${versions["spring-test-cont"]}")
    testImplementation("org.testcontainers:junit-jupiter:${versions["testcontainers"]}")
    testImplementation("org.testcontainers:postgresql:${versions["testcontainers"]}")
}

tasks.test {
    useJUnitPlatform()
}

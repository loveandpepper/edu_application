package org.hofftech.edu;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public abstract class AbstractPostgresContainer {

    final static String POSTGRES_VERSION = "postgres:latest";
    final static String DATABASE_NAME = "postgres";
    final static String DATABASE_USER = "postgres";
    final static String DATABASE_PASSWORD = "postgres";

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>(POSTGRES_VERSION)
            .withDatabaseName(DATABASE_NAME)
            .withUsername(DATABASE_USER)
            .withPassword(DATABASE_PASSWORD);

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = "jdbc:postgresql://"
                + POSTGRES_CONTAINER.getHost()+ ":" + POSTGRES_CONTAINER.getMappedPort(5432)
                + "/" + POSTGRES_CONTAINER.getDatabaseName();

        registry.add("spring.datasource.url", () -> jdbcUrl);
    }
}

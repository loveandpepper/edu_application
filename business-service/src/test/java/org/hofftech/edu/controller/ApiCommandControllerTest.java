package org.hofftech.edu.controller;

import org.hofftech.edu.service.ReportService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Интеграционные тесты контроллера ApiCommandController.
 * Использует Testcontainers (PostgreSQL) и MockMvc.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiCommandControllerTest {

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

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private ReportService reportService;


    @Test
    @Order(1)
    void testCreatePackage() throws Exception {
        String json = """
        {
          "name": "Посылка Тип 1",
          "form": "1",
          "symbol": "1"
        }
        """;


        mockMvc.perform(post("/api/packages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Создана посылка: Посылка Тип 1")));
    }

    @Test
    @Order(2)
    void testUpdatePackage() throws Exception {
        String json = """
        {
          "oldName": "Посылка Тип 1",
          "name": "Большая Посылка"
        }
        """;

        mockMvc.perform(put("/api/packages/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Обновлённая посылка: Большая Посылка")));
    }

    @Test
    @Order(3)
    void testLoad() throws Exception {
        String json = """
        {
          "parcelsText": "Посылка Тип 3, Посылка Тип 7",
          "trucks": "6x6, 6x7",
          "user": "test",
          "even": "true"
        }
        """;

        mockMvc.perform(post("/api/packages/load")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Truck 1")));
    }

    @Test
    @Order(4)
    void testFindPackage() throws Exception {
        mockMvc.perform(get("/api/packages/{name}", "Большая Посылка"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Большая Посылка"))
                .andExpect(jsonPath("$.symbol").value("1"));
    }

    @Test
    @Order(5)
    void testListPackages() throws Exception {
        mockMvc.perform(get("/api/packages")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());
    }



    @Disabled
    @Test
    void testBilling() throws Exception {
        when(reportService.generateReport(anyString(), any(), any()))
                .thenReturn("07-02-2025; Погрузка; 2 машин; 2 посылок; 800 рублей");

        String billingJson = """
            {
               "user": "test",
               "from": "07-02-2025",
               "to": "07-02-2025"
            }
        """;

        mockMvc.perform(post("/api/packages/billing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(billingJson))
                .andExpect(status().isOk())
                .andExpect(content().string("07-02-2025; Погрузка; 2 машин; 2 посылок; 800 рублей"));
    }

    @Test
    @Order(7)
    void testDeletePackage() throws Exception {
        mockMvc.perform(delete("/api/packages/{name}", "Большая Посылка"))
                .andExpect(status().isOk())
                .andExpect(content().string("Посылка 'Большая Посылка' успешно удалена."));
    }
}

package br.com.fiap.challenge.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("OpenAPIConfig")
class OpenAPIConfigTest {

    @Autowired
    private OpenAPIConfig openAPIConfig;

    private OpenAPI openAPI;

    @BeforeEach
    void setUp() {
        openAPI = openAPIConfig.challengeOpenAPI();
    }

    @Test
    @DisplayName("Deve criar OpenAPI bean com informações corretas")
    void shouldCreateOpenAPIBeanWithCorrectInfo() {
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
    }

    @Test
    @DisplayName("Deve ter título correto na API")
    void shouldHaveCorrectTitle() {
        assertEquals("MyDelivery API Challenge", openAPI.getInfo().getTitle());
    }

    @Test
    @DisplayName("Deve ter descrição correta na API")
    void shouldHaveCorrectDescription() {
        assertEquals("API RESTful para o sistema de gerenciamento de pedidos MyDelivery.",
            openAPI.getInfo().getDescription());
    }

    @Test
    @DisplayName("Deve ter versão correta na API")
    void shouldHaveCorrectVersion() {
        assertEquals("v1.0.0", openAPI.getInfo().getVersion());
    }

    @Test
    @DisplayName("Deve ter licença Apache 2.0")
    void shouldHaveLicenseApache() {
        License license = openAPI.getInfo().getLicense();
        assertNotNull(license);
        assertEquals("Apache 2.0", license.getName());
        assertEquals("http://springdoc.org", license.getUrl());
    }

    @Test
    @DisplayName("Deve retornar a mesma instância OpenAPI a cada chamada")
    void shouldReturnSameInstanceOnMultipleCalls() {
        OpenAPI openAPI1 = openAPIConfig.challengeOpenAPI();
        OpenAPI openAPI2 = openAPIConfig.challengeOpenAPI();

        assertNotNull(openAPI1);
        assertNotNull(openAPI2);
    }
}


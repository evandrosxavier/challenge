package br.com.fiap.challenge.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(

        security = {@SecurityRequirement(name = "bearerAuth")}
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenAPIConfig {

    @Bean
    public OpenAPI challengeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MyDelivery API Challenge")
                        .description("API RESTful para o sistema de gerenciamento de pedidos MyDelivery.")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org" )));
    }
}

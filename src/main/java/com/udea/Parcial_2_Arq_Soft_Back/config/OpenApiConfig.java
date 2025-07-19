package com.udea.Parcial_2_Arq_Soft_Back.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Historias Clínicas")
                        .version("v1.0")
                        .description("API RESTful para gestión de historias clínicas hospitalarias con HATEOAS")
                        .contact(new Contact()
                                .name("Universidad de Antioquia")
                                .email("contacto@udea.edu.co")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desarrollo"),
                        new Server()
                                .url("http://localhost:8080/data-api")
                                .description("Spring Data REST API")
                ));
    }
}
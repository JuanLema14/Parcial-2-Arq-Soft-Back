package com.udea.Parcial_2_Arq_Soft_Back.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@Tag(name = "API Root", description = "Punto de entrada principal de la API")
@CrossOrigin(origins = "*")
public class ApiRootController {

    @GetMapping(value = {"/api", "/api/"})  // Ambas variantes
    @Operation(summary = "API Root",
            description = "Punto de entrada principal con enlaces a todos los recursos")
    public ResponseEntity<Map<String, Object>> root() {
        log.info("GET /api - Acceso al root de la API");

        Map<String, Object> response = new HashMap<>();
        response.put("message", "API de Historias Clínicas - Hospital UdeA");
        response.put("version", "v1.0");
        response.put("description", "API RESTful para gestión de historias clínicas hospitalarias");

        // Enlaces simples sin HATEOAS complejo
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("doctores", "http://localhost:8080/api/doctores");
        endpoints.put("pacientes", "http://localhost:8080/api/pacientes");
        endpoints.put("historias_clinicas", "http://localhost:8080/api/historias-clinicas");
        endpoints.put("swagger_ui", "http://localhost:8080/swagger-ui.html");
        endpoints.put("api_docs", "http://localhost:8080/v3/api-docs");

        response.put("_links", endpoints);

        return ResponseEntity.ok(response);
    }
}
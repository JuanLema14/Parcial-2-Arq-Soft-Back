package com.udea.Parcial_2_Arq_Soft_Back.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api")
@Slf4j
@Tag(name = "API Root", description = "Punto de entrada principal de la API")
@CrossOrigin(origins = "*")
public class ApiRootController {

    @GetMapping
    @Operation(summary = "API Root",
            description = "Punto de entrada principal con enlaces HATEOAS a todos los recursos")
    public ResponseEntity<ApiRootResponse> root() {
        log.info("GET /api - Acceso al root de la API");

        ApiRootResponse response = new ApiRootResponse();

        response.add(linkTo(methodOn(ApiRootController.class).root()).withSelfRel());
        response.add(linkTo(methodOn(PacienteController.class).getAllPacientes()).withRel("pacientes")
                .withTitle("Gestión de pacientes"));
        response.add(linkTo(methodOn(DoctorController.class).getAllDoctores()).withRel("doctores")
                .withTitle("Gestión de doctores"));
        response.add(linkTo(methodOn(HistoriaClinicaController.class).getAllHistoriasClinicas()).withRel("historias-clinicas")
                .withTitle("Gestión de historias clínicas"));

        return ResponseEntity.ok(response);
    }

    static class ApiRootResponse extends RepresentationModel<ApiRootResponse> {
        private final String message = "API de Historias Clínicas - Hospital UdeA";
        private final String version = "v1.0";
        private final String description = "API RESTful para gestión de historias clínicas hospitalarias";

        public String getMessage() { return message; }
        public String getVersion() { return version; }
        public String getDescription() { return description; }
    }
}
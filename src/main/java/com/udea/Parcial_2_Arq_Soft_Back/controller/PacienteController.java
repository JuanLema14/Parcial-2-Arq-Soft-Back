package com.udea.Parcial_2_Arq_Soft_Back.controller;

import com.udea.Parcial_2_Arq_Soft_Back.model.dto.CrearPacienteRequest;
import com.udea.Parcial_2_Arq_Soft_Back.model.dto.PacienteDto;
import com.udea.Parcial_2_Arq_Soft_Back.model.dto.PacienteConHistoriasDto;
import com.udea.Parcial_2_Arq_Soft_Back.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pacientes", description = "API para gestión de pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    private final PacienteService pacienteService;

    @GetMapping("/pacientes")
    @Operation(summary = "Obtener todos los pacientes",
            description = "Obtiene la lista completa de pacientes registrados")
    public ResponseEntity<CollectionModel<EntityModel<PacienteDto>>> getAllPacientes() {
        log.info("GET /api/pacientes - Obteniendo todos los pacientes");

        List<PacienteDto> pacientes = pacienteService.getAllPacientes();

        List<EntityModel<PacienteDto>> pacienteModels = pacientes.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<PacienteDto>> collectionModel = CollectionModel.of(pacienteModels);

        collectionModel.add(linkTo(methodOn(PacienteController.class).getAllPacientes()).withSelfRel());
        collectionModel.add(linkTo(methodOn(PacienteController.class).createPaciente(null)).withRel("create"));

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/pacientes/{cedula}")
    @Operation(summary = "Obtener paciente por cédula",
            description = "Obtiene un paciente específico por su número de cédula")
    public ResponseEntity<EntityModel<PacienteDto>> getPacienteByCedula(@PathVariable String cedula) {
        log.info("GET /api/pacientes/{} - Obteniendo paciente por cédula", cedula);

        PacienteDto paciente = pacienteService.getPacienteByCedula(cedula);
        EntityModel<PacienteDto> pacienteModel = toEntityModel(paciente);

        return ResponseEntity.ok(pacienteModel);
    }

    @GetMapping("/pacientes/{cedula}/historias-clinicas")
    @Operation(summary = "Obtener paciente con historias clínicas",
            description = "Obtiene un paciente con todas sus historias clínicas por número de cédula")
    public ResponseEntity<EntityModel<PacienteConHistoriasDto>> getPacienteConHistoriasByCedula(@PathVariable String cedula) {
        log.info("GET /api/pacientes/{}/historias-clinicas - Obteniendo paciente con historias", cedula);

        PacienteConHistoriasDto pacienteConHistorias = pacienteService.getPacienteConHistoriasByCedula(cedula);
        EntityModel<PacienteConHistoriasDto> pacienteModel = toEntityModelConHistorias(pacienteConHistorias);

        return ResponseEntity.ok(pacienteModel);
    }

    @PostMapping("/pacientes")
    @Operation(summary = "Crear nuevo paciente",
            description = "Registra un nuevo paciente en el sistema")
    public ResponseEntity<EntityModel<PacienteDto>> createPaciente(@Valid @RequestBody CrearPacienteRequest request) {
        log.info("POST /api/pacientes - Creando nuevo paciente");

        PacienteDto createdPaciente = pacienteService.createPaciente(request);
        EntityModel<PacienteDto> pacienteModel = toEntityModel(createdPaciente);

        return ResponseEntity.created(pacienteModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(pacienteModel);
    }

    @PutMapping("/pacientes/{cedula}")
    @Operation(summary = "Actualizar paciente",
            description = "Actualiza la información de un paciente existente")
    public ResponseEntity<EntityModel<PacienteDto>> updatePaciente(
            @PathVariable String cedula,
            @Valid @RequestBody PacienteDto pacienteDto) {
        log.info("PUT /api/pacientes/{} - Actualizando paciente", cedula);

        PacienteDto updatedPaciente = pacienteService.updatePaciente(cedula, pacienteDto);
        EntityModel<PacienteDto> pacienteModel = toEntityModel(updatedPaciente);

        return ResponseEntity.ok(pacienteModel);
    }

    private EntityModel<PacienteDto> toEntityModel(PacienteDto paciente) {
        return EntityModel.of(paciente)
                .add(linkTo(methodOn(PacienteController.class).getPacienteByCedula(paciente.getCedula())).withSelfRel())
                .add(linkTo(methodOn(PacienteController.class).getAllPacientes()).withRel("pacientes"))
                .add(linkTo(methodOn(PacienteController.class).updatePaciente(paciente.getCedula(), null)).withRel("update"))
                .add(linkTo(methodOn(PacienteController.class).getPacienteConHistoriasByCedula(paciente.getCedula()))
                        .withRel("historias-clinicas").withTitle("Ver historias clínicas"))
                .add(linkTo(methodOn(HistoriaClinicaController.class).createHistoriaClinica(null))
                        .withRel("crear-historia").withTitle("Crear historia clínica"));
    }

    private EntityModel<PacienteConHistoriasDto> toEntityModelConHistorias(PacienteConHistoriasDto paciente) {
        return EntityModel.of(paciente)
                .add(linkTo(methodOn(PacienteController.class).getPacienteConHistoriasByCedula(paciente.getCedula())).withSelfRel())
                .add(linkTo(methodOn(PacienteController.class).getPacienteByCedula(paciente.getCedula())).withRel("paciente"))
                .add(linkTo(methodOn(PacienteController.class).getAllPacientes()).withRel("pacientes"))
                .add(linkTo(methodOn(HistoriaClinicaController.class).getHistoriasByPacienteCedula(paciente.getCedula()))
                        .withRel("solo-historias").withTitle("Solo historias clínicas"))
                .add(linkTo(methodOn(HistoriaClinicaController.class).createHistoriaClinica(null))
                        .withRel("crear-historia").withTitle("Crear nueva historia clínica"));
    }
}
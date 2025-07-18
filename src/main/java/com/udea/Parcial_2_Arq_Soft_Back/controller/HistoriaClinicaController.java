package com.udea.Parcial_2_Arq_Soft_Back.controller;

import com.udea.Parcial_2_Arq_Soft_Back.model.dto.CrearHistoriaClinicaRequest;
import com.udea.Parcial_2_Arq_Soft_Back.model.dto.HistoriaClinicaDto;
import com.udea.Parcial_2_Arq_Soft_Back.service.HistoriaClinicaService;
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
@Tag(name = "Historias Clínicas", description = "API para gestión de historias clínicas")
@CrossOrigin(origins = "*")
public class HistoriaClinicaController {

    private final HistoriaClinicaService historiaClinicaService;

    @GetMapping("/historias-clinicas")
    @Operation(summary = "Obtener todas las historias clínicas",
            description = "Obtiene la lista completa de historias clínicas registradas")
    public ResponseEntity<CollectionModel<EntityModel<HistoriaClinicaDto>>> getAllHistoriasClinicas() {
        log.info("GET /api/historias-clinicas - Obteniendo todas las historias clínicas");

        List<HistoriaClinicaDto> historias = historiaClinicaService.getAllHistoriasClinicas();

        List<EntityModel<HistoriaClinicaDto>> historiaModels = historias.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<HistoriaClinicaDto>> collectionModel = CollectionModel.of(historiaModels);

        collectionModel.add(linkTo(methodOn(HistoriaClinicaController.class).getAllHistoriasClinicas()).withSelfRel());
        collectionModel.add(linkTo(methodOn(HistoriaClinicaController.class).createHistoriaClinica(null)).withRel("create"));
        collectionModel.add(linkTo(methodOn(PacienteController.class).getAllPacientes()).withRel("pacientes"));
        collectionModel.add(linkTo(methodOn(DoctorController.class).getAllDoctores()).withRel("doctores"));

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/historias-clinicas/paciente/{cedula}")
    @Operation(summary = "Obtener historias clínicas por cédula de paciente",
            description = "Obtiene todas las historias clínicas de un paciente específico por su cédula")
    public ResponseEntity<CollectionModel<EntityModel<HistoriaClinicaDto>>> getHistoriasByPacienteCedula(@PathVariable String cedula) {
        log.info("GET /api/historias-clinicas/paciente/{} - Obteniendo historias por cédula de paciente", cedula);

        List<HistoriaClinicaDto> historias = historiaClinicaService.getHistoriasByPacienteCedula(cedula);

        List<EntityModel<HistoriaClinicaDto>> historiaModels = historias.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<HistoriaClinicaDto>> collectionModel = CollectionModel.of(historiaModels);

        collectionModel.add(linkTo(methodOn(HistoriaClinicaController.class).getHistoriasByPacienteCedula(cedula)).withSelfRel());
        collectionModel.add(linkTo(methodOn(PacienteController.class).getPacienteByCedula(cedula)).withRel("paciente"));
        collectionModel.add(linkTo(methodOn(PacienteController.class).getPacienteConHistoriasByCedula(cedula))
                .withRel("paciente-completo").withTitle("Paciente con historias"));
        collectionModel.add(linkTo(methodOn(HistoriaClinicaController.class).createHistoriaClinica(null))
                .withRel("crear-historia").withTitle("Crear nueva historia"));

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/historias-clinicas/{id}")
    @Operation(summary = "Obtener historia clínica por ID",
            description = "Obtiene una historia clínica específica por su identificador")
    public ResponseEntity<EntityModel<HistoriaClinicaDto>> getHistoriaClinicaById(@PathVariable Long id) {
        log.info("GET /api/historias-clinicas/{} - Obteniendo historia clínica por ID", id);

        HistoriaClinicaDto historia = historiaClinicaService.getHistoriaClinicaById(id);
        EntityModel<HistoriaClinicaDto> historiaModel = toEntityModel(historia);

        return ResponseEntity.ok(historiaModel);
    }

    @PostMapping("/historias-clinicas")
    @Operation(summary = "Crear nueva historia clínica",
            description = "Registra una nueva historia clínica para un paciente con un doctor específico")
    public ResponseEntity<EntityModel<HistoriaClinicaDto>> createHistoriaClinica(@Valid @RequestBody CrearHistoriaClinicaRequest request) {
        log.info("POST /api/historias-clinicas - Creando nueva historia clínica para paciente: {}",
                request.getPacienteCedula());

        HistoriaClinicaDto createdHistoria = historiaClinicaService.createHistoriaClinica(request);
        EntityModel<HistoriaClinicaDto> historiaModel = toEntityModel(createdHistoria);

        return ResponseEntity.created(historiaModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(historiaModel);
    }

    @PutMapping("/historias-clinicas/{id}")
    @Operation(summary = "Actualizar historia clínica",
            description = "Actualiza la información de una historia clínica existente")
    public ResponseEntity<EntityModel<HistoriaClinicaDto>> updateHistoriaClinica(
            @PathVariable Long id,
            @Valid @RequestBody CrearHistoriaClinicaRequest request) {
        log.info("PUT /api/historias-clinicas/{} - Actualizando historia clínica", id);

        HistoriaClinicaDto updatedHistoria = historiaClinicaService.updateHistoriaClinica(id, request);
        EntityModel<HistoriaClinicaDto> historiaModel = toEntityModel(updatedHistoria);

        return ResponseEntity.ok(historiaModel);
    }

    private EntityModel<HistoriaClinicaDto> toEntityModel(HistoriaClinicaDto historia) {
        return EntityModel.of(historia)
                .add(linkTo(methodOn(HistoriaClinicaController.class).getHistoriaClinicaById(historia.getId())).withSelfRel())
                .add(linkTo(methodOn(HistoriaClinicaController.class).getAllHistoriasClinicas()).withRel("historias-clinicas"))
                .add(linkTo(methodOn(HistoriaClinicaController.class).updateHistoriaClinica(historia.getId(), null)).withRel("update"))
                .add(linkTo(methodOn(PacienteController.class).getPacienteByCedula(historia.getPaciente().getCedula()))
                        .withRel("paciente").withTitle("Ver paciente"))
                .add(linkTo(methodOn(DoctorController.class).getDoctorByCedula(historia.getDoctor().getCedula()))
                        .withRel("doctor").withTitle("Ver doctor"))
                .add(linkTo(methodOn(HistoriaClinicaController.class).getHistoriasByPacienteCedula(historia.getPaciente().getCedula()))
                        .withRel("todas-historias-paciente").withTitle("Todas las historias del paciente"));
    }
}
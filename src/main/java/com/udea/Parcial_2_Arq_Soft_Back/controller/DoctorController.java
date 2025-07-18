package com.udea.Parcial_2_Arq_Soft_Back.controller;

import com.udea.Parcial_2_Arq_Soft_Back.model.dto.DoctorDto;
import com.udea.Parcial_2_Arq_Soft_Back.service.DoctorService;
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
@Tag(name = "Doctores", description = "API para gestión de doctores")
@CrossOrigin(origins = "*")
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping("/doctores")
    @Operation(summary = "Obtener todos los doctores",
            description = "Obtiene la lista completa de doctores registrados")
    public ResponseEntity<CollectionModel<EntityModel<DoctorDto>>> getAllDoctores() {
        log.info("GET /api/doctores - Obteniendo todos los doctores");

        List<DoctorDto> doctores = doctorService.getAllDoctores();

        List<EntityModel<DoctorDto>> doctorModels = doctores.stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<DoctorDto>> collectionModel = CollectionModel.of(doctorModels);

        collectionModel.add(linkTo(methodOn(DoctorController.class).getAllDoctores()).withSelfRel());
        collectionModel.add(linkTo(methodOn(DoctorController.class).createDoctor(null)).withRel("create"));

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/doctores/{cedula}")
    @Operation(summary = "Obtener doctor por cédula",
            description = "Obtiene un doctor específico por su número de cédula")
    public ResponseEntity<EntityModel<DoctorDto>> getDoctorByCedula(@PathVariable String cedula) {
        log.info("GET /api/doctores/{} - Obteniendo doctor por cédula", cedula);

        DoctorDto doctor = doctorService.getDoctorByCedula(cedula);
        EntityModel<DoctorDto> doctorModel = toEntityModel(doctor);

        return ResponseEntity.ok(doctorModel);
    }

    @PostMapping("/doctores")
    @Operation(summary = "Crear nuevo doctor",
            description = "Registra un nuevo doctor en el sistema")
    public ResponseEntity<EntityModel<DoctorDto>> createDoctor(@Valid @RequestBody DoctorDto doctorDto) {
        log.info("POST /api/doctores - Creando nuevo doctor");

        DoctorDto createdDoctor = doctorService.createDoctor(doctorDto);
        EntityModel<DoctorDto> doctorModel = toEntityModel(createdDoctor);

        return ResponseEntity.created(doctorModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(doctorModel);
    }

    @PutMapping("/doctores/{cedula}")
    @Operation(summary = "Actualizar doctor",
            description = "Actualiza la información de un doctor existente")
    public ResponseEntity<EntityModel<DoctorDto>> updateDoctor(
            @PathVariable String cedula,
            @Valid @RequestBody DoctorDto doctorDto) {
        log.info("PUT /api/doctores/{} - Actualizando doctor", cedula);

        DoctorDto updatedDoctor = doctorService.updateDoctor(cedula, doctorDto);
        EntityModel<DoctorDto> doctorModel = toEntityModel(updatedDoctor);

        return ResponseEntity.ok(doctorModel);
    }

    private EntityModel<DoctorDto> toEntityModel(DoctorDto doctor) {
        return EntityModel.of(doctor)
                .add(linkTo(methodOn(DoctorController.class).getDoctorByCedula(doctor.getCedula())).withSelfRel())
                .add(linkTo(methodOn(DoctorController.class).getAllDoctores()).withRel("doctores"))
                .add(linkTo(methodOn(DoctorController.class).updateDoctor(doctor.getCedula(), null)).withRel("update"))
                .add(linkTo(methodOn(HistoriaClinicaController.class).getHistoriasByPacienteCedula(null))
                        .withRel("historias-clinicas").withTitle("Ver historias clínicas"));
    }
}
package com.udea.Parcial_2_Arq_Soft_Back.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaClinicaDto extends RepresentationModel<HistoriaClinicaDto> {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("paciente")
    private PacienteDto paciente;

    @JsonProperty("doctor")
    private DoctorDto doctor;

    @JsonProperty("motivo_consulta")
    private String motivoConsulta;

    @JsonProperty("diagnostico")
    private String diagnostico;

    @JsonProperty("tratamiento")
    private String tratamiento;

    @JsonProperty("observaciones")
    private String observaciones;

    @JsonProperty("fecha_registro")
    private LocalDateTime fechaRegistro;
}
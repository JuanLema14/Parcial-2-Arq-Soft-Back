package com.udea.Parcial_2_Arq_Soft_Back.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearHistoriaClinicaRequest {

    @JsonProperty("paciente_cedula")
    @NotBlank(message = "La cédula del paciente es obligatoria")
    @Pattern(regexp = "^[0-9]{6,20}$", message = "La cédula debe contener solo números y tener entre 6 y 20 dígitos")
    private String pacienteCedula;

    @JsonProperty("doctor_cedula")
    @NotBlank(message = "La cédula del doctor es obligatoria")
    @Pattern(regexp = "^[0-9]{6,20}$", message = "La cédula debe contener solo números y tener entre 6 y 20 dígitos")
    private String doctorCedula;

    @JsonProperty("motivo_consulta")
    @NotBlank(message = "El motivo de consulta es obligatorio")
    private String motivoConsulta;

    @JsonProperty("diagnostico")
    private String diagnostico;

    @JsonProperty("tratamiento")
    private String tratamiento;

    @JsonProperty("observaciones")
    private String observaciones;
}
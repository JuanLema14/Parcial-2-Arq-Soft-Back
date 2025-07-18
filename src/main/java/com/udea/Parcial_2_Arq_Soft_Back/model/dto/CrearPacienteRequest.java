package com.udea.Parcial_2_Arq_Soft_Back.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearPacienteRequest {

    @JsonProperty("cedula")
    @NotBlank(message = "La cédula del paciente es obligatoria")
    @Pattern(regexp = "^[0-9]{6,20}$", message = "La cédula debe contener solo números y tener entre 6 y 20 dígitos")
    private String cedula;

    @JsonProperty("nombre")
    @NotBlank(message = "El nombre del paciente es obligatorio")
    private String nombre;

    @JsonProperty("edad")
    @Min(value = 0, message = "La edad debe ser mayor o igual a 0")
    private Integer edad;

    @JsonProperty("genero")
    @Pattern(regexp = "^(Masculino|Femenino|Otro)$", message = "El género debe ser: Masculino, Femenino u Otro")
    private String genero;

    @JsonProperty("telefono")
    private String telefono;

    @JsonProperty("email")
    private String email;

    @JsonProperty("direccion")
    private String direccion;
}
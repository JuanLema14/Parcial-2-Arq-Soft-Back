package com.udea.Parcial_2_Arq_Soft_Back.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearDoctorRequest {

    @JsonProperty("cedula")
    @NotBlank(message = "La cédula del doctor es obligatoria")
    @Pattern(regexp = "^[0-9]{6,20}$", message = "La cédula debe contener solo números y tener entre 6 y 20 dígitos")
    private String cedula;

    @JsonProperty("nombre")
    @NotBlank(message = "El nombre del doctor es obligatorio")
    private String nombre;

    @JsonProperty("especialidad")
    private String especialidad;

    @JsonProperty("telefono")
    @Pattern(regexp = "^[0-9+\\-\\s()]{7,20}$", message = "Formato de teléfono inválido")
    private String telefono;

    @JsonProperty("email")
    @Email(message = "Formato de email inválido")
    private String email;
}
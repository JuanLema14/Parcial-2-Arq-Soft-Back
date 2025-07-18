package com.udea.Parcial_2_Arq_Soft_Back.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDto extends RepresentationModel<PacienteDto> {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("cedula")
    private String cedula;

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("edad")
    private Integer edad;

    @JsonProperty("genero")
    private String genero;

    @JsonProperty("telefono")
    private String telefono;

    @JsonProperty("email")
    private String email;

    @JsonProperty("direccion")
    private String direccion;
}
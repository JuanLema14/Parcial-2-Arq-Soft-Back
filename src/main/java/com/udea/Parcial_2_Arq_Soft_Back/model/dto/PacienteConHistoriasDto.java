package com.udea.Parcial_2_Arq_Soft_Back.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PacienteConHistoriasDto extends RepresentationModel<PacienteConHistoriasDto> {

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

    @JsonProperty("historias_clinicas")
    private List<HistoriaClinicaSimpleDto> historiasClinicas;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoriaClinicaSimpleDto {
        @JsonProperty("id")
        private Long id;

        @JsonProperty("doctor_cedula")
        private String doctorCedula;

        @JsonProperty("doctor_nombre")
        private String doctorNombre;

        @JsonProperty("motivo_consulta")
        private String motivoConsulta;

        @JsonProperty("diagnostico")
        private String diagnostico;

        @JsonProperty("tratamiento")
        private String tratamiento;

        @JsonProperty("observaciones")
        private String observaciones;

        @JsonProperty("fecha_registro")
        private java.time.LocalDateTime fechaRegistro;
    }
}
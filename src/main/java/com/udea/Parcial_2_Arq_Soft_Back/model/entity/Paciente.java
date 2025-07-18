package com.udea.Parcial_2_Arq_Soft_Back.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "pacientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "historiasClinicas")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    @NotBlank(message = "La cédula del paciente es obligatoria")
    @Pattern(regexp = "^[0-9]{6,20}$", message = "La cédula debe contener solo números y tener entre 6 y 20 dígitos")
    private String cedula;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "El nombre del paciente es obligatorio")
    private String nombre;

    @Column(nullable = false)
    @Min(value = 0, message = "La edad debe ser mayor o igual a 0")
    private Integer edad;

    @Column(length = 10)
    @Pattern(regexp = "^(Masculino|Femenino|Otro)$", message = "Debe ingresar un género válido: Masculino, Femenino u Otro")
    private String genero;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String email;

    @Column(length = 200)
    private String direccion;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoriaClinica> historiasClinicas;
}
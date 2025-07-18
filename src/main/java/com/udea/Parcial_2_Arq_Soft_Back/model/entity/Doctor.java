package com.udea.Parcial_2_Arq_Soft_Back.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "doctores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "historiasClinicas")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    @NotBlank(message = "La cédula del doctor es obligatoria")
    @Pattern(regexp = "^[0-9]{6,20}$", message = "La cédula debe contener solo números y tener entre 6 y 20 dígitos")
    private String cedula;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "El nombre del doctor es obligatorio")
    private String nombre;

    @Column(length = 100)
    private String especialidad;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String email;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoriaClinica> historiasClinicas;
}
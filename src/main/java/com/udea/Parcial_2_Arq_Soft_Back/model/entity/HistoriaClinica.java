package com.udea.Parcial_2_Arq_Soft_Back.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "historias_clinicas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriaClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    @NotNull(message = "El paciente es obligatorio")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @NotNull(message = "El doctor es obligatorio")
    private Doctor doctor;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "El motivo de consulta es obligatorio")
    private String motivoConsulta;

    @Column(columnDefinition = "TEXT")
    private String diagnostico;

    @Column(columnDefinition = "TEXT")
    private String tratamiento;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_registro", nullable = false)
    @CreationTimestamp
    private LocalDateTime fechaRegistro;
}
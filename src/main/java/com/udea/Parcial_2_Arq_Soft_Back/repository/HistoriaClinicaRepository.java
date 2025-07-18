package com.udea.Parcial_2_Arq_Soft_Back.repository;

import com.udea.Parcial_2_Arq_Soft_Back.model.entity.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Long> {

    @Query("SELECT hc FROM HistoriaClinica hc " +
            "JOIN FETCH hc.paciente p " +
            "JOIN FETCH hc.doctor d " +
            "WHERE p.cedula = :cedula " +
            "ORDER BY hc.fechaRegistro DESC")
    List<HistoriaClinica> findHistoriasByPacienteCedula(@Param("cedula") String cedula);

    @Query("SELECT hc FROM HistoriaClinica hc " +
            "JOIN FETCH hc.paciente " +
            "JOIN FETCH hc.doctor " +
            "WHERE hc.paciente.id = :pacienteId " +
            "ORDER BY hc.fechaRegistro DESC")
    List<HistoriaClinica> findHistoriasByPacienteId(@Param("pacienteId") Long pacienteId);
}
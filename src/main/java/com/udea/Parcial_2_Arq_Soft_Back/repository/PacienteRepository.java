package com.udea.Parcial_2_Arq_Soft_Back.repository;

import com.udea.Parcial_2_Arq_Soft_Back.model.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByCedula(String cedula);

    boolean existsByCedula(String cedula);

    @Query("SELECT p FROM Paciente p LEFT JOIN FETCH p.historiasClinicas hc " +
            "LEFT JOIN FETCH hc.doctor WHERE p.cedula = :cedula")
    Optional<Paciente> findPacienteWithHistoriasByCedula(@Param("cedula") String cedula);
}
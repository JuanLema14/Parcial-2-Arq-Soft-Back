package com.udea.Parcial_2_Arq_Soft_Back.repository;

import com.udea.Parcial_2_Arq_Soft_Back.model.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByCedula(String cedula);

    boolean existsByCedula(String cedula);

    @Query("SELECT d FROM Doctor d WHERE d.cedula = :cedula")
    Optional<Doctor> findDoctorByCedula(@Param("cedula") String cedula);
}
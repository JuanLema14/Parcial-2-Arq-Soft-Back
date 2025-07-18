package com.udea.Parcial_2_Arq_Soft_Back.service;

import com.udea.Parcial_2_Arq_Soft_Back.exception.ResourceNotFoundException;
import com.udea.Parcial_2_Arq_Soft_Back.exception.ValidationException;
import com.udea.Parcial_2_Arq_Soft_Back.model.dto.DoctorDto;
import com.udea.Parcial_2_Arq_Soft_Back.model.entity.Doctor;
import com.udea.Parcial_2_Arq_Soft_Back.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;

    @Transactional(readOnly = true)
    public List<DoctorDto> getAllDoctores() {
        log.info("Obteniendo todos los doctores");
        return doctorRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DoctorDto getDoctorByCedula(String cedula) {
        log.info("Buscando doctor con cédula: {}", cedula);
        Doctor doctor = doctorRepository.findByCedula(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con cédula: " + cedula));
        return convertToDto(doctor);
    }

    @Transactional(readOnly = true)
    public DoctorDto getDoctorById(Long id) {
        log.info("Buscando doctor con ID: {}", id);
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con ID: " + id));
        return convertToDto(doctor);
    }

    public DoctorDto createDoctor(DoctorDto doctorDto) {
        log.info("Creando nuevo doctor con cédula: {}", doctorDto.getCedula());

        if (doctorRepository.existsByCedula(doctorDto.getCedula())) {
            throw new ValidationException("Ya existe un doctor con la cédula: " + doctorDto.getCedula());
        }

        Doctor doctor = convertToEntity(doctorDto);
        Doctor savedDoctor = doctorRepository.save(doctor);
        log.info("Doctor creado exitosamente con ID: {}", savedDoctor.getId());
        return convertToDto(savedDoctor);
    }

    public DoctorDto updateDoctor(String cedula, DoctorDto doctorDto) {
        log.info("Actualizando doctor con cédula: {}", cedula);

        Doctor existingDoctor = doctorRepository.findByCedula(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con cédula: " + cedula));

        // Verificar que no se repita la cédula
        if (!existingDoctor.getCedula().equals(doctorDto.getCedula()) &&
                doctorRepository.existsByCedula(doctorDto.getCedula())) {
            throw new ValidationException("Ya existe un doctor con la cédula: " + doctorDto.getCedula());
        }

        updateDoctorFields(existingDoctor, doctorDto);
        Doctor updatedDoctor = doctorRepository.save(existingDoctor);
        log.info("Doctor actualizado exitosamente con ID: {}", updatedDoctor.getId());
        return convertToDto(updatedDoctor);
    }

    private void updateDoctorFields(Doctor existingDoctor, DoctorDto doctorDto) {
        if (doctorDto.getCedula() != null && !doctorDto.getCedula().trim().isEmpty()) {
            existingDoctor.setCedula(doctorDto.getCedula().trim());
        }
        if (doctorDto.getNombre() != null && !doctorDto.getNombre().trim().isEmpty()) {
            existingDoctor.setNombre(doctorDto.getNombre().trim());
        }
        if (doctorDto.getEspecialidad() != null) {
            existingDoctor.setEspecialidad(doctorDto.getEspecialidad().trim());
        }
        if (doctorDto.getTelefono() != null) {
            existingDoctor.setTelefono(doctorDto.getTelefono().trim());
        }
        if (doctorDto.getEmail() != null) {
            existingDoctor.setEmail(doctorDto.getEmail().trim());
        }
    }

    private DoctorDto convertToDto(Doctor doctor) {
        return new DoctorDto(
                doctor.getId(),
                doctor.getCedula(),
                doctor.getNombre(),
                doctor.getEspecialidad(),
                doctor.getTelefono(),
                doctor.getEmail()
        );
    }

    private Doctor convertToEntity(DoctorDto doctorDto) {
        Doctor doctor = new Doctor();
        doctor.setCedula(doctorDto.getCedula());
        doctor.setNombre(doctorDto.getNombre());
        doctor.setEspecialidad(doctorDto.getEspecialidad());
        doctor.setTelefono(doctorDto.getTelefono());
        doctor.setEmail(doctorDto.getEmail());
        return doctor;
    }
}
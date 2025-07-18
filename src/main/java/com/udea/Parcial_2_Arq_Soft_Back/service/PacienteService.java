package com.udea.Parcial_2_Arq_Soft_Back.service;

import com.udea.Parcial_2_Arq_Soft_Back.exception.ResourceNotFoundException;
import com.udea.Parcial_2_Arq_Soft_Back.exception.ValidationException;
import com.udea.Parcial_2_Arq_Soft_Back.model.dto.CrearPacienteRequest;
import com.udea.Parcial_2_Arq_Soft_Back.model.dto.PacienteDto;
import com.udea.Parcial_2_Arq_Soft_Back.model.dto.PacienteConHistoriasDto;
import com.udea.Parcial_2_Arq_Soft_Back.model.entity.Paciente;
import com.udea.Parcial_2_Arq_Soft_Back.repository.PacienteRepository;
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
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    @Transactional(readOnly = true)
    public List<PacienteDto> getAllPacientes() {
        log.info("Obteniendo todos los pacientes");
        return pacienteRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PacienteDto getPacienteByCedula(String cedula) {
        log.info("Buscando paciente con cédula: {}", cedula);
        Paciente paciente = pacienteRepository.findByCedula(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con cédula: " + cedula));
        return convertToDto(paciente);
    }

    @Transactional(readOnly = true)
    public PacienteConHistoriasDto getPacienteConHistoriasByCedula(String cedula) {
        log.info("Buscando paciente con historias clínicas para cédula: {}", cedula);
        Paciente paciente = pacienteRepository.findPacienteWithHistoriasByCedula(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con cédula: " + cedula));
        return convertToPacienteConHistoriasDto(paciente);
    }

    @Transactional(readOnly = true)
    public PacienteDto getPacienteById(Long id) {
        log.info("Buscando paciente con ID: {}", id);
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con ID: " + id));
        return convertToDto(paciente);
    }

    public PacienteDto createPaciente(CrearPacienteRequest request) {
        log.info("Creando nuevo paciente con cédula: {}", request.getCedula());

        if (pacienteRepository.existsByCedula(request.getCedula())) {
            throw new ValidationException("Ya existe un paciente con la cédula: " + request.getCedula());
        }

        Paciente paciente = convertToEntity(request);
        Paciente savedPaciente = pacienteRepository.save(paciente);
        log.info("Paciente creado exitosamente con ID: {}", savedPaciente.getId());
        return convertToDto(savedPaciente);
    }

    public PacienteDto updatePaciente(String cedula, PacienteDto pacienteDto) {
        log.info("Actualizando paciente con cédula: {}", cedula);

        Paciente existingPaciente = pacienteRepository.findByCedula(cedula)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con cédula: " + cedula));

        // Verificar que no se repita la cédula
        if (!existingPaciente.getCedula().equals(pacienteDto.getCedula()) &&
                pacienteRepository.existsByCedula(pacienteDto.getCedula())) {
            throw new ValidationException("Ya existe un paciente con la cédula: " + pacienteDto.getCedula());
        }

        updatePacienteFields(existingPaciente, pacienteDto);
        Paciente updatedPaciente = pacienteRepository.save(existingPaciente);
        log.info("Paciente actualizado exitosamente con ID: {}", updatedPaciente.getId());
        return convertToDto(updatedPaciente);
    }

    private void updatePacienteFields(Paciente existingPaciente, PacienteDto pacienteDto) {
        if (pacienteDto.getCedula() != null && !pacienteDto.getCedula().trim().isEmpty()) {
            existingPaciente.setCedula(pacienteDto.getCedula().trim());
        }
        if (pacienteDto.getNombre() != null && !pacienteDto.getNombre().trim().isEmpty()) {
            existingPaciente.setNombre(pacienteDto.getNombre().trim());
        }
        if (pacienteDto.getEdad() != null) {
            existingPaciente.setEdad(pacienteDto.getEdad());
        }
        if (pacienteDto.getGenero() != null) {
            existingPaciente.setGenero(pacienteDto.getGenero().trim());
        }
        if (pacienteDto.getTelefono() != null) {
            existingPaciente.setTelefono(pacienteDto.getTelefono().trim());
        }
        if (pacienteDto.getEmail() != null) {
            existingPaciente.setEmail(pacienteDto.getEmail().trim());
        }
        if (pacienteDto.getDireccion() != null) {
            existingPaciente.setDireccion(pacienteDto.getDireccion().trim());
        }
    }

    private PacienteDto convertToDto(Paciente paciente) {
        return new PacienteDto(
                paciente.getId(),
                paciente.getCedula(),
                paciente.getNombre(),
                paciente.getEdad(),
                paciente.getGenero(),
                paciente.getTelefono(),
                paciente.getEmail(),
                paciente.getDireccion()
        );
    }

    private PacienteConHistoriasDto convertToPacienteConHistoriasDto(Paciente paciente) {
        List<PacienteConHistoriasDto.HistoriaClinicaSimpleDto> historiasDto =
                paciente.getHistoriasClinicas().stream()
                        .map(hc -> new PacienteConHistoriasDto.HistoriaClinicaSimpleDto(
                                hc.getId(),
                                hc.getDoctor().getCedula(),
                                hc.getDoctor().getNombre(),
                                hc.getMotivoConsulta(),
                                hc.getDiagnostico(),
                                hc.getTratamiento(),
                                hc.getObservaciones(),
                                hc.getFechaRegistro()
                        ))
                        .collect(Collectors.toList());

        return new PacienteConHistoriasDto(
                paciente.getId(),
                paciente.getCedula(),
                paciente.getNombre(),
                paciente.getEdad(),
                paciente.getGenero(),
                paciente.getTelefono(),
                paciente.getEmail(),
                paciente.getDireccion(),
                historiasDto
        );
    }

    private Paciente convertToEntity(CrearPacienteRequest request) {
        Paciente paciente = new Paciente();
        paciente.setCedula(request.getCedula());
        paciente.setNombre(request.getNombre());
        paciente.setEdad(request.getEdad());
        paciente.setGenero(request.getGenero());
        paciente.setTelefono(request.getTelefono());
        paciente.setEmail(request.getEmail());
        paciente.setDireccion(request.getDireccion());
        return paciente;
    }
}
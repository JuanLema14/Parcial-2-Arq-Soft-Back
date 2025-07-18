package com.udea.Parcial_2_Arq_Soft_Back.service;

import com.udea.Parcial_2_Arq_Soft_Back.exception.ResourceNotFoundException;
import com.udea.Parcial_2_Arq_Soft_Back.model.dto.CrearHistoriaClinicaRequest;
import com.udea.Parcial_2_Arq_Soft_Back.model.dto.DoctorDto;
import com.udea.Parcial_2_Arq_Soft_Back.model.dto.HistoriaClinicaDto;
import com.udea.Parcial_2_Arq_Soft_Back.model.dto.PacienteDto;
import com.udea.Parcial_2_Arq_Soft_Back.model.entity.Doctor;
import com.udea.Parcial_2_Arq_Soft_Back.model.entity.HistoriaClinica;
import com.udea.Parcial_2_Arq_Soft_Back.model.entity.Paciente;
import com.udea.Parcial_2_Arq_Soft_Back.repository.DoctorRepository;
import com.udea.Parcial_2_Arq_Soft_Back.repository.HistoriaClinicaRepository;
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
public class HistoriaClinicaService {

    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final PacienteRepository pacienteRepository;
    private final DoctorRepository doctorRepository;

    @Transactional(readOnly = true)
    public List<HistoriaClinicaDto> getAllHistoriasClinicas() {
        log.info("Obteniendo todas las historias clínicas");
        return historiaClinicaRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HistoriaClinicaDto> getHistoriasByPacienteCedula(String cedula) {
        log.info("Obteniendo historias clínicas para paciente con cédula: {}", cedula);

        // Verificar que el paciente existe
        if (!pacienteRepository.existsByCedula(cedula)) {
            throw new ResourceNotFoundException("Paciente no encontrado con cédula: " + cedula);
        }

        return historiaClinicaRepository.findHistoriasByPacienteCedula(cedula).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HistoriaClinicaDto getHistoriaClinicaById(Long id) {
        log.info("Buscando historia clínica con ID: {}", id);
        HistoriaClinica historiaClinica = historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historia clínica no encontrada con ID: " + id));
        return convertToDto(historiaClinica);
    }

    public HistoriaClinicaDto createHistoriaClinica(CrearHistoriaClinicaRequest request) {
        log.info("Creando nueva historia clínica para paciente: {} con doctor: {}",
                request.getPacienteCedula(), request.getDoctorCedula());

        // Buscar paciente
        Paciente paciente = pacienteRepository.findByCedula(request.getPacienteCedula())
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con cédula: " + request.getPacienteCedula()));

        // Buscar doctor
        Doctor doctor = doctorRepository.findByCedula(request.getDoctorCedula())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con cédula: " + request.getDoctorCedula()));

        HistoriaClinica historiaClinica = new HistoriaClinica();
        historiaClinica.setPaciente(paciente);
        historiaClinica.setDoctor(doctor);
        historiaClinica.setMotivoConsulta(request.getMotivoConsulta());
        historiaClinica.setDiagnostico(request.getDiagnostico());
        historiaClinica.setTratamiento(request.getTratamiento());
        historiaClinica.setObservaciones(request.getObservaciones());

        HistoriaClinica savedHistoria = historiaClinicaRepository.save(historiaClinica);
        log.info("Historia clínica creada exitosamente con ID: {}", savedHistoria.getId());
        return convertToDto(savedHistoria);
    }

    public HistoriaClinicaDto updateHistoriaClinica(Long id, CrearHistoriaClinicaRequest request) {
        log.info("Actualizando historia clínica con ID: {}", id);

        HistoriaClinica existingHistoria = historiaClinicaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historia clínica no encontrada con ID: " + id));

        // Actualizar paciente si es diferente
        if (!existingHistoria.getPaciente().getCedula().equals(request.getPacienteCedula())) {
            Paciente nuevoPaciente = pacienteRepository.findByCedula(request.getPacienteCedula())
                    .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con cédula: " + request.getPacienteCedula()));
            existingHistoria.setPaciente(nuevoPaciente);
        }

        // Actualizar doctor si es diferente
        if (!existingHistoria.getDoctor().getCedula().equals(request.getDoctorCedula())) {
            Doctor nuevoDoctor = doctorRepository.findByCedula(request.getDoctorCedula())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado con cédula: " + request.getDoctorCedula()));
            existingHistoria.setDoctor(nuevoDoctor);
        }

        updateHistoriaClinicaFields(existingHistoria, request);
        HistoriaClinica updatedHistoria = historiaClinicaRepository.save(existingHistoria);
        log.info("Historia clínica actualizada exitosamente con ID: {}", updatedHistoria.getId());
        return convertToDto(updatedHistoria);
    }

    private void updateHistoriaClinicaFields(HistoriaClinica existingHistoria, CrearHistoriaClinicaRequest request) {
        if (request.getMotivoConsulta() != null && !request.getMotivoConsulta().trim().isEmpty()) {
            existingHistoria.setMotivoConsulta(request.getMotivoConsulta().trim());
        }
        if (request.getDiagnostico() != null) {
            existingHistoria.setDiagnostico(request.getDiagnostico().trim());
        }
        if (request.getTratamiento() != null) {
            existingHistoria.setTratamiento(request.getTratamiento().trim());
        }
        if (request.getObservaciones() != null) {
            existingHistoria.setObservaciones(request.getObservaciones().trim());
        }
    }

    private HistoriaClinicaDto convertToDto(HistoriaClinica historiaClinica) {
        PacienteDto pacienteDto = new PacienteDto(
                historiaClinica.getPaciente().getId(),
                historiaClinica.getPaciente().getCedula(),
                historiaClinica.getPaciente().getNombre(),
                historiaClinica.getPaciente().getEdad(),
                historiaClinica.getPaciente().getGenero(),
                historiaClinica.getPaciente().getTelefono(),
                historiaClinica.getPaciente().getEmail(),
                historiaClinica.getPaciente().getDireccion()
        );

        DoctorDto doctorDto = new DoctorDto(
                historiaClinica.getDoctor().getId(),
                historiaClinica.getDoctor().getCedula(),
                historiaClinica.getDoctor().getNombre(),
                historiaClinica.getDoctor().getEspecialidad(),
                historiaClinica.getDoctor().getTelefono(),
                historiaClinica.getDoctor().getEmail()
        );

        return new HistoriaClinicaDto(
                historiaClinica.getId(),
                pacienteDto,
                doctorDto,
                historiaClinica.getMotivoConsulta(),
                historiaClinica.getDiagnostico(),
                historiaClinica.getTratamiento(),
                historiaClinica.getObservaciones(),
                historiaClinica.getFechaRegistro()
        );
    }
}
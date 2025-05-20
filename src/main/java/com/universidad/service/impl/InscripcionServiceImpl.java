package com.universidad.service.impl;

import com.universidad.model.Inscripcion;
import com.universidad.model.Estudiante;
import com.universidad.model.Materia;
import com.universidad.repository.InscripcionRepository;
import com.universidad.repository.EstudianteRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IInscripcionService;
import com.universidad.dto.InscripcionDTO;
import com.universidad.validation.InscripcionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InscripcionServiceImpl implements IInscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final EstudianteRepository estudianteRepository;
    private final MateriaRepository materiaRepository;
    private final InscripcionValidator inscripcionValidator;

    @Autowired
    public InscripcionServiceImpl(InscripcionRepository inscripcionRepository,
                                EstudianteRepository estudianteRepository,
                                MateriaRepository materiaRepository,
                                InscripcionValidator inscripcionValidator) {
        this.inscripcionRepository = inscripcionRepository;
        this.estudianteRepository = estudianteRepository;
        this.materiaRepository = materiaRepository;
        this.inscripcionValidator = inscripcionValidator;
    }

    private InscripcionDTO mapToDTO(Inscripcion inscripcion) {
        if (inscripcion == null) return null;
        return InscripcionDTO.builder()
                .id(inscripcion.getId())
                .id_estudiante(inscripcion.getEstudiante().getId())
                .id_materia(inscripcion.getMateria().getId())
                .fechaInscripcion(inscripcion.getFechaInscripcion())
                .paralelo(inscripcion.getParalelo())
                .build();
    }

    private Inscripcion mapToEntity(InscripcionDTO dto) {
        Estudiante estudiante = estudianteRepository.findById(dto.getId_estudiante())
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado"));
        
        Materia materia = materiaRepository.findById(dto.getId_materia())
                .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada"));
        
        return Inscripcion.builder()
                .id(dto.getId())
                .estudiante(estudiante)
                .materia(materia)
                .fechaInscripcion(dto.getFechaInscripcion())
                .paralelo(dto.getParalelo())
                .build();
    }

    @Override
    @Cacheable(value = "inscripciones")
    public List<InscripcionDTO> obtenerTodasLasInscripciones() {
        return inscripcionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "inscripcion", key = "#id")
    public InscripcionDTO obtenerInscripcionPorId(Long id) {
        return inscripcionRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    @Cacheable(value = "inscripciones", key = "#id_estudiante")
    public List<InscripcionDTO> obtenerInscripcionesPorEstudiante(Long id_estudiante) {
        return inscripcionRepository.findByEstudianteId(id_estudiante).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @CachePut(value = "inscripcion", key = "#result.id")
    @CacheEvict(value = "inscripciones", allEntries = true)
    public InscripcionDTO crearInscripcion(InscripcionDTO inscripcionDTO) {
        inscripcionValidator.validacionCompletaInscripcion(inscripcionDTO, 30); // Capacidad máxima de 30 por paralelo
        
        Inscripcion inscripcion = mapToEntity(inscripcionDTO);
        inscripcion.setFechaInscripcion(inscripcionDTO.getFechaInscripcion() != null ? 
                inscripcionDTO.getFechaInscripcion() : LocalDate.now());
        
        Inscripcion savedInscripcion = inscripcionRepository.save(inscripcion);
        return mapToDTO(savedInscripcion);
    }

    @Override
    @CachePut(value = "inscripcion", key = "#id")
    @CacheEvict(value = "inscripciones", allEntries = true)
    public InscripcionDTO actualizarInscripcion(Long id, InscripcionDTO inscripcionDTO) {        
        Inscripcion inscripcionExistente = inscripcionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada"));
                
        inscripcionValidator.validarFormatoParalelo(inscripcionDTO.getParalelo());
        inscripcionValidator.validarFechaInscripcion(inscripcionDTO.getFechaInscripcion(), LocalDate.now());
        
        Inscripcion inscripcionActualizada = mapToEntity(inscripcionDTO);
        inscripcionActualizada.setId(id);
        
        Inscripcion updatedInscripcion = inscripcionRepository.save(inscripcionActualizada);
        return mapToDTO(updatedInscripcion);
    }

    @Override
    @CacheEvict(value = {"inscripcion", "inscripciones"}, allEntries = true)
    public void eliminarInscripcion(Long id) {        
        if (!inscripcionRepository.existsById(id)) {
            throw new IllegalArgumentException("Inscripción no encontrada");
        }
        inscripcionRepository.deleteById(id);
    }
}
package com.universidad.service.impl;

import com.universidad.dto.AsignacionDocenteDTO;
import com.universidad.model.AsignacionDocente;
import com.universidad.model.Docente;
import com.universidad.model.Materia;
import com.universidad.repository.AsignacionDocenteRepository;
import com.universidad.repository.DocenteRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IAsignacionDocenteService;
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
public class AsignacionDocenteServiceImpl implements IAsignacionDocenteService {

    @Autowired
    private AsignacionDocenteRepository asignacionRepository;

    @Autowired
    private DocenteRepository docenteRepository;
    
    @Autowired
    private MateriaRepository materiaRepository;

    private AsignacionDocenteDTO mapToDTO(AsignacionDocente asignacion) {
        if (asignacion == null) return null;
        return AsignacionDocenteDTO.builder()
                .id(asignacion.getId())
                .id_docente(asignacion.getDocente().getId())
                .id_materia(asignacion.getMateria().getId())
                .horario(asignacion.getHorario())
                .paralelo(asignacion.getParalelo())
                .fechaAsignacion(asignacion.getFechaAsignacion())
                .periodoAcademico(asignacion.getPeriodoAcademico())
                .build();
    }

    private AsignacionDocente mapToEntity(AsignacionDocenteDTO dto) {
        Docente docente = docenteRepository.findById(dto.getId_docente())
                .orElseThrow(() -> new IllegalArgumentException("Docente no encontrado"));
        
        Materia materia = materiaRepository.findById(dto.getId_materia())
                .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada"));
        
        return AsignacionDocente.builder()
                .id(dto.getId())
                .docente(docente)
                .materia(materia)
                .horario(dto.getHorario())
                .paralelo(dto.getParalelo())
                .fechaAsignacion(dto.getFechaAsignacion())
                .periodoAcademico(dto.getPeriodoAcademico())
                .build();
    }

    @Override
    @Cacheable(value = "asignaciones")
    public List<AsignacionDocenteDTO> obtenerTodasLasAsignaciones() {
        return asignacionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "asignacion", key = "#id")
    public AsignacionDocenteDTO obtenerAsignacionPorId(Long id) {
        return asignacionRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    @Cacheable(value = "asignaciones", key = "#id_docente")
    public List<AsignacionDocenteDTO> obtenerAsignacionesPorDocente(Long id_docente) {
        return asignacionRepository.findByDocenteId(id_docente).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @CachePut(value = "asignacion", key = "#result.id")
    @CacheEvict(value = "asignaciones", allEntries = true)
    public AsignacionDocenteDTO crearAsignacion(AsignacionDocenteDTO asignacionDTO) {
        AsignacionDocente asignacion = mapToEntity(asignacionDTO);
        asignacion.setFechaAsignacion(asignacionDTO.getFechaAsignacion() != null ? 
                asignacionDTO.getFechaAsignacion() : LocalDate.now());
        
        AsignacionDocente saved = asignacionRepository.save(asignacion);
        return mapToDTO(saved);
    }

    @Override
    @CachePut(value = "asignacion", key = "#id")
    @CacheEvict(value = "asignaciones", allEntries = true)
    public AsignacionDocenteDTO actualizarAsignacion(Long id, AsignacionDocenteDTO asignacionDTO) {
        AsignacionDocente asignacionExistente = asignacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asignación no encontrada"));
        
        AsignacionDocente asignacionActualizada = mapToEntity(asignacionDTO);
        asignacionActualizada.setId(id);
        
        AsignacionDocente updated = asignacionRepository.save(asignacionActualizada);
        return mapToDTO(updated);
    }

    @Override
    @CacheEvict(value = {"asignacion", "asignaciones"}, allEntries = true)
    public void eliminarAsignacion(Long id) {
        asignacionRepository.deleteById(id);
    }
}
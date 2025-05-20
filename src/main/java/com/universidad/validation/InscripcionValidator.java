package com.universidad.validation;

import org.springframework.stereotype.Component;
import com.universidad.dto.InscripcionDTO;
import com.universidad.repository.InscripcionRepository;
import com.universidad.repository.EstudianteRepository;
import com.universidad.repository.MateriaRepository;
import java.time.LocalDate;

@Component
public class InscripcionValidator {

    private final InscripcionRepository inscripcionRepository;
    private final EstudianteRepository estudianteRepository;
    private final MateriaRepository materiaRepository;

    public InscripcionValidator(InscripcionRepository inscripcionRepository,
                               EstudianteRepository estudianteRepository,
                               MateriaRepository materiaRepository) {
        this.inscripcionRepository = inscripcionRepository;
        this.estudianteRepository = estudianteRepository;
        this.materiaRepository = materiaRepository;
    }

    public void validarEstudianteExistente(Long idEstudiante) {
        if (!estudianteRepository.existsById(idEstudiante)) {
            throw new BusinessException("No existe un estudiante con el ID proporcionado");
        }
    }

    public void validarMateriaExistente(Long idMateria) {
        if (!materiaRepository.existsById(idMateria)) {
            throw new BusinessException("No existe una materia con el ID proporcionado");
        }
    }
    
    public void validarInscripcionUnica(Long idEstudiante, Long idMateria) {
        if (inscripcionRepository.existsByEstudianteIdAndMateriaId(idEstudiante, idMateria)) {
            throw new BusinessException("El estudiante ya está inscrito en esta materia");
        }
    }

    public void validarCapacidadParalelo(String paralelo, Long idMateria, int capacidadMaxima) {
        long inscripcionesEnParalelo = inscripcionRepository.countByMateriaIdAndParalelo(idMateria, paralelo);
        if (inscripcionesEnParalelo >= capacidadMaxima) {
            throw new BusinessException("El paralelo " + paralelo + " ha alcanzado su capacidad máxima");
        }
    }

    public void validarFechaInscripcion(LocalDate fechaInscripcion, LocalDate fechaLimite) {
        if (fechaInscripcion !=null && fechaInscripcion.isAfter(fechaLimite)) {
            throw new BusinessException("La fecha de inscripción no puede ser posterior a " + fechaLimite);
        }
    }

    public void validarFormatoParalelo(String paralelo) {
        if (!paralelo.matches("^[A-Za-z]\\d{0,2}$")) {
            throw new BusinessException("El paralelo debe comenzar con una letra seguida de máximo 2 números (ej: A1, B12)");
        }
    }

    public void validacionCompletaInscripcion(InscripcionDTO inscripcionDTO, int capacidadMaximaParalelo) {
        validarEstudianteExistente(inscripcionDTO.getId_estudiante());
        validarMateriaExistente(inscripcionDTO.getId_materia());
        validarInscripcionUnica(inscripcionDTO.getId_estudiante(), inscripcionDTO.getId_materia());
        validarFormatoParalelo(inscripcionDTO.getParalelo());
        validarCapacidadParalelo(inscripcionDTO.getParalelo(), inscripcionDTO.getId_materia(), capacidadMaximaParalelo);
        validarFechaInscripcion(inscripcionDTO.getFechaInscripcion(), LocalDate.now().plusDays(1));
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}
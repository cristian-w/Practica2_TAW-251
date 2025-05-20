package com.universidad.validation;

import com.universidad.dto.AsignacionDocenteDTO;
import com.universidad.repository.AsignacionDocenteRepository;
import com.universidad.repository.DocenteRepository;
import com.universidad.repository.MateriaRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class AsignacionDocenteValidator {

    private final AsignacionDocenteRepository asignacionRepository;
    private final DocenteRepository docenteRepository;
    private final MateriaRepository materiaRepository;

    private static final Set<String> DIAS_VALIDOS = new HashSet<>(Arrays.asList(
            "lunes", "martes", "miercoles", "jueves", "viernes", "sabado"
    ));

    public AsignacionDocenteValidator(AsignacionDocenteRepository asignacionRepository,
                                    DocenteRepository docenteRepository,
                                    MateriaRepository materiaRepository) {
        this.asignacionRepository = asignacionRepository;
        this.docenteRepository = docenteRepository;
        this.materiaRepository = materiaRepository;
    }

    public void validarDocenteExistente(Long idDocente) {
        if (!docenteRepository.existsById(idDocente)) {
            throw new BusinessException("No existe un docente con el ID proporcionado");
        }
    }

    public void validarMateriaExistente(Long idMateria) {
        if (!materiaRepository.existsById(idMateria)) {
            throw new BusinessException("No existe una materia con el ID proporcionado");
        }
    }
    
    public void validarDisponibilidadDocente(Long idDocente, String horario) {
        if (asignacionRepository.existsByDocenteIdAndHorarioContaining(idDocente, extraerDiaDeHorario(horario))) {
            throw new BusinessException("El docente ya tiene una asignación en este día");
        }
    }

    public void validarFormatoHorario(String horario) {
        if (!Pattern.matches("^([A-Za-z]+\\s+\\d{1,2}:\\d{2}-\\d{1,2}:\\d{2}(,\\s*[A-Za-z]+\\s+\\d{1,2}:\\d{2}-\\d{1,2}:\\d{2})*$", horario)) {
            throw new BusinessException("Formato de horario inválido. Ejemplo válido: Lunes 08:00-10:00, Martes 09:00-11:00");
        }
        String[] bloques = horario.split(",\\s*");
        for (String bloque : bloques) {
            String[] partes = bloque.split("\\s+");
            if (partes.length != 2) {
                throw new BusinessException("Formato de bloque horario inválido: " + bloque);
            }

            String dia = partes[0].toLowerCase();
            if (!DIAS_VALIDOS.contains(dia)) {
                throw new BusinessException("Día no válido: " + partes[0]);
            }

            try {
                String[] horas = partes[1].split("-");
                LocalTime.parse(horas[0]);
                LocalTime.parse(horas[1]);
            } catch (DateTimeParseException e) {
                throw new BusinessException("Formato de hora inválido en: " + partes[1]);
            }
        }
    }

    public void validarFechaAsignacion(LocalDate fechaAsignacion) {
        if (fechaAsignacion != null && fechaAsignacion.isAfter(LocalDate.now())) {
            throw new BusinessException("La fecha de asignación no puede ser futura");
        }
    }

    public void validarPeriodoAcademico(String periodo) {
        if (!Pattern.matches("^\\d{4}-[1-2]$", periodo)) {
            throw new BusinessException("El período académico debe tener formato YYYY-1 o YYYY-2");
        }
    }

    public void validacionCompleta(AsignacionDocenteDTO asignacionDTO, int capacidadMaximaParalelo) {
        validarDocenteExistente(asignacionDTO.getId_docente());
        validarMateriaExistente(asignacionDTO.getId_materia());
        validarFormatoHorario(asignacionDTO.getHorario());
        validarDisponibilidadDocente(asignacionDTO.getId_docente(), asignacionDTO.getHorario());
        validarFechaAsignacion(asignacionDTO.getFechaAsignacion());
        validarPeriodoAcademico(asignacionDTO.getPeriodoAcademico());
    }

    private String extraerDiaDeHorario(String horario) {
        return horario.split("\\s+")[0].toLowerCase();
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}
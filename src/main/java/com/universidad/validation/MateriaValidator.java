package com.universidad.validation;

import com.universidad.dto.MateriaDTO;
import com.universidad.repository.MateriaRepository;

import org.springframework.stereotype.Component;

@Component
public class MateriaValidator {

    private final MateriaRepository materiaRepository;

    public MateriaValidator(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    public void validaCodigoUnico(String codigoUnico) {
        if (materiaRepository.existsByCodigoUnico(codigoUnico)) {
            throw new IllegalArgumentException("Ya existe una materia con este código único.");
        }
    }

    public void validaNombreMateria(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la materia no puede estar vacío.");
        }
        if (nombre.length() > 100) {
            throw new IllegalArgumentException("El nombre de la materia no debe superar los 100 caracteres.");
        }
    }

    public void validaCodigoFormato(String codigoUnico) {
        if (!codigoUnico.matches("^[A-Z]{3}-\\d{3}$")) {
            throw new IllegalArgumentException("El código único debe tener el formato ABC-123.");
        }
    }

    public void validacionCompletaMateria(MateriaDTO materiaDTO) {
        validaNombreMateria(materiaDTO.getNombreMateria());
        validaCodigoFormato(materiaDTO.getCodigoUnico());
        validaCodigoUnico(materiaDTO.getCodigoUnico());
    }

    public static class BusinessException extends RuntimeException {
        public BusinessException(String message) {
            super(message);
        }
    }
}

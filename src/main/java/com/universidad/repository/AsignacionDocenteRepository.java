package com.universidad.repository;

import com.universidad.model.AsignacionDocente;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AsignacionDocenteRepository extends JpaRepository<AsignacionDocente, Long> {
    List<AsignacionDocente> findByDocenteId(Long id_docente);    
    boolean existsByDocenteIdAndHorarioContaining(Long docenteId, String diaHorario);           
    Optional<AsignacionDocente> findById(Long id);
}
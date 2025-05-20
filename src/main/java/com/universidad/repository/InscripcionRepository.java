package com.universidad.repository;

import com.universidad.model.Inscripcion;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import java.util.List;
import java.util.Optional;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> findByEstudianteId(Long id_estudiante);
    boolean existsByEstudianteIdAndMateriaId(Long id_estudiante, Long idMateria);
    long countByMateriaIdAndParalelo(Long idMateria, String paralelo);    
    Optional<Inscripcion> findById(Long id);
}

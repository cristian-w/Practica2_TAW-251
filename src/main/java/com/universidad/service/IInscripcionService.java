package com.universidad.service;

import com.universidad.dto.InscripcionDTO;
import java.util.List;

public interface IInscripcionService {
    List<InscripcionDTO> obtenerTodasLasInscripciones();
    InscripcionDTO obtenerInscripcionPorId(Long id);
    List<InscripcionDTO> obtenerInscripcionesPorEstudiante(Long id_estudiante);
    InscripcionDTO crearInscripcion(InscripcionDTO inscripcion);
    InscripcionDTO actualizarInscripcion(Long id, InscripcionDTO inscripcion);
    void eliminarInscripcion(Long id);
}
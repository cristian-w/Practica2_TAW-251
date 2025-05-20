package com.universidad.service;

import com.universidad.dto.AsignacionDocenteDTO;
import java.util.List;

public interface IAsignacionDocenteService {
    AsignacionDocenteDTO crearAsignacion(AsignacionDocenteDTO asignacionDTO);
    AsignacionDocenteDTO actualizarAsignacion(Long id, AsignacionDocenteDTO asignacionDTO);
    void eliminarAsignacion(Long id);
    AsignacionDocenteDTO obtenerAsignacionPorId(Long id);
    List<AsignacionDocenteDTO> obtenerAsignacionesPorDocente(Long idDocente);
    List<AsignacionDocenteDTO> obtenerTodasLasAsignaciones();
}
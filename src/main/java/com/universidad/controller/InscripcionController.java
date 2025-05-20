package com.universidad.controller;

import com.universidad.dto.InscripcionDTO;
import com.universidad.service.IInscripcionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/inscripciones")
@Validated
@Tag(name = "Inscripciones", description = "Operaciones relacionadas con la inscripción de estudiantes en materias")
public class InscripcionController {

    private final IInscripcionService inscripcionService;
    private static final Logger logger = LoggerFactory.getLogger(InscripcionController.class);

    @Autowired
    public InscripcionController(IInscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    @Operation(summary = "Obtener todas las inscripciones")
    @GetMapping
    public ResponseEntity<List<InscripcionDTO>> obtenerTodasLasInscripciones() {
        long inicio = System.currentTimeMillis();
        logger.info("[INSCRIPCION] Inicio obtenerTodasLasInscripciones: {}", inicio);
        List<InscripcionDTO> result = inscripcionService.obtenerTodasLasInscripciones();
        long fin = System.currentTimeMillis();
        logger.info("[INSCRIPCION] Fin obtenerTodasLasInscripciones: {} (Duración: {} ms)", fin, (fin - inicio));
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Obtener una inscripción por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<InscripcionDTO> obtenerInscripcionPorId(
            @Parameter(description = "ID de la inscripción") @PathVariable @Positive Long id) {
        long inicio = System.currentTimeMillis();
        logger.info("[INSCRIPCION] Inicio obtenerInscripcionPorId: {}", inicio);
        InscripcionDTO inscripcion = inscripcionService.obtenerInscripcionPorId(id);
        long fin = System.currentTimeMillis();
        logger.info("[INSCRIPCION] Fin obtenerInscripcionPorId: {} (Duración: {} ms)", fin, (fin - inicio));
        if (inscripcion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inscripcion);
    }

    @Operation(summary = "Obtener inscripciones por ID de estudiante")
    @GetMapping("/estudiante/{id}")
    public ResponseEntity<List<InscripcionDTO>> obtenerInscripcionesPorEstudiante(
            @Parameter(description = "ID del estudiante") @PathVariable(name = "id") @Positive Long id_estudiante) {
        List<InscripcionDTO> inscripciones = inscripcionService.obtenerInscripcionesPorEstudiante(id_estudiante);
        if (inscripciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(inscripciones);
    }

    @Operation(summary = "Crear una nueva inscripción")
    @PostMapping
    public ResponseEntity<InscripcionDTO> crearInscripcion(
            @Parameter(description = "Datos de la inscripción") @Valid @RequestBody InscripcionDTO inscripcionDTO) {
        InscripcionDTO nuevaInscripcion = inscripcionService.crearInscripcion(inscripcionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaInscripcion);
    }

    @Operation(summary = "Actualizar una inscripción existente")
    @PutMapping("/{id}")
    public ResponseEntity<InscripcionDTO> actualizarInscripcion(
            @Parameter(description = "ID de la inscripción a actualizar") @PathVariable @Positive Long id,
            @Parameter(description = "Datos actualizados de la inscripción") @Valid @RequestBody InscripcionDTO inscripcionDTO) {
        InscripcionDTO actualizada = inscripcionService.actualizarInscripcion(id, inscripcionDTO);
        return ResponseEntity.ok(actualizada);
    }

    @Operation(summary = "Eliminar una inscripción por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInscripcion(
            @Parameter(description = "ID de la inscripción a eliminar") @PathVariable @Positive Long id) {
        inscripcionService.eliminarInscripcion(id);
        return ResponseEntity.noContent().build();
    }
}

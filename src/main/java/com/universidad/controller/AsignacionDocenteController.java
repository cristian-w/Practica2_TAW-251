package com.universidad.controller;

import com.universidad.dto.AsignacionDocenteDTO;
import com.universidad.service.IAsignacionDocenteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/asignaciones-docentes")
@Validated
@RequiredArgsConstructor
@Tag(name = "Asignaciones Docentes", description = "Operaciones para la gestión de asignaciones de docentes a materias")
public class AsignacionDocenteController {

    private static final Logger logger = LoggerFactory.getLogger(AsignacionDocenteController.class);
    private final IAsignacionDocenteService asignacionService;

    @PostMapping
    @Operation(summary = "Crear nueva asignación", description = "Registra una nueva asignación de docente a materia")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Asignación creada exitosamente",
                     content = @Content(schema = @Schema(implementation = AsignacionDocenteDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Conflicto: docente ya asignado en ese horario")
    })
    public ResponseEntity<AsignacionDocenteDTO> crearAsignacion(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la asignación a crear",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AsignacionDocenteDTO.class)))
            @Valid @RequestBody AsignacionDocenteDTO asignacionDTO) {
        logger.info("[ASIGNACION] Creando nueva asignación: {}", asignacionDTO);
        AsignacionDocenteDTO nueva = asignacionService.crearAsignacion(asignacionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar asignación", description = "Actualiza los datos de una asignación existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Asignación actualizada exitosamente",
                     content = @Content(schema = @Schema(implementation = AsignacionDocenteDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Asignación no encontrada")
    })
    public ResponseEntity<AsignacionDocenteDTO> actualizarAsignacion(
            @Parameter(description = "ID de la asignación a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos actualizados de la asignación",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AsignacionDocenteDTO.class)))
            @Valid @RequestBody AsignacionDocenteDTO asignacionDTO) {
        logger.info("[ASIGNACION] Actualizando asignación con ID {}: {}", id, asignacionDTO);
        AsignacionDocenteDTO actualizada = asignacionService.actualizarAsignacion(id, asignacionDTO);
        return ResponseEntity.ok(actualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar asignación", description = "Elimina una asignación docente-materia")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Asignación eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Asignación no encontrada")
    })
    public ResponseEntity<Void> eliminarAsignacion(
            @Parameter(description = "ID de la asignación a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        logger.info("[ASIGNACION] Eliminando asignación con ID {}", id);
        asignacionService.eliminarAsignacion(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener asignación por ID", description = "Recupera los detalles de una asignación específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Asignación encontrada",
                     content = @Content(schema = @Schema(implementation = AsignacionDocenteDTO.class))),
        @ApiResponse(responseCode = "404", description = "Asignación no encontrada")
    })
    public ResponseEntity<AsignacionDocenteDTO> obtenerPorId(
            @Parameter(description = "ID de la asignación a consultar", example = "1", required = true)
            @PathVariable Long id) {
        logger.info("[ASIGNACION] Consultando asignación con ID {}", id);
        AsignacionDocenteDTO dto = asignacionService.obtenerAsignacionPorId(id);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/docente/{id_docente}")
    @Operation(summary = "Obtener asignaciones por docente", description = "Lista todas las asignaciones de un docente específico")
    @ApiResponse(responseCode = "200", description = "Lista de asignaciones del docente",
                 content = @Content(schema = @Schema(implementation = AsignacionDocenteDTO.class)))
    public ResponseEntity<List<AsignacionDocenteDTO>> obtenerPorDocente(
            @Parameter(description = "ID del docente", example = "101", required = true)
            @PathVariable Long id_docente) {
        logger.info("[ASIGNACION] Listando asignaciones del docente ID {}", id_docente);
        List<AsignacionDocenteDTO> asignaciones = asignacionService.obtenerAsignacionesPorDocente(id_docente);
        return ResponseEntity.ok(asignaciones);
    }

    @GetMapping
    @Operation(summary = "Listar todas las asignaciones", description = "Recupera todas las asignaciones docentes registradas")
    @ApiResponse(responseCode = "200", description = "Lista completa de asignaciones",
                 content = @Content(schema = @Schema(implementation = AsignacionDocenteDTO.class)))
    public ResponseEntity<List<AsignacionDocenteDTO>> obtenerTodas() {
        logger.info("[ASIGNACION] Listando todas las asignaciones docentes");
        List<AsignacionDocenteDTO> asignaciones = asignacionService.obtenerTodasLasAsignaciones();
        return ResponseEntity.ok(asignaciones);
    }
}

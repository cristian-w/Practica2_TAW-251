package com.universidad.controller;

import com.universidad.dto.EstudianteDTO;
import com.universidad.model.Estudiante;
import com.universidad.service.IEstudianteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/estudiantes")
@Validated
@RequiredArgsConstructor
@Tag(name = "Estudiantes", description = "Operaciones para la gestión de estudiantes")
public class EstudianteController {

    private final IEstudianteService estudianteService;
    private static final Logger logger = LoggerFactory.getLogger(EstudianteController.class);

    @GetMapping
    @Operation(summary = "Listar todos los estudiantes", description = "Retorna todos los estudiantes registrados")
    @ApiResponse(responseCode = "200", description = "Lista obtenida",
                 content = @Content(schema = @Schema(implementation = EstudianteDTO.class)))
    public ResponseEntity<List<EstudianteDTO>> obtenerTodosLosEstudiantes() {
        logger.info("[ESTUDIANTE] Listando todos los estudiantes");
        List<EstudianteDTO> estudiantes = estudianteService.obtenerTodosLosEstudiantes();
        return ResponseEntity.ok(estudiantes);
    }

    @GetMapping("/inscripcion/{numeroInscripcion}")
    @Operation(summary = "Buscar por número de inscripción", description = "Busca un estudiante por número de inscripción")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudiante encontrado",
                     content = @Content(schema = @Schema(implementation = EstudianteDTO.class))),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    public ResponseEntity<EstudianteDTO> obtenerEstudiantePorNumeroInscripcion(
            @Parameter(description = "Número de inscripción", example = "INS-2023-001", required = true)
            @PathVariable String numeroInscripcion) {
        logger.info("[ESTUDIANTE] Buscando estudiante con inscripción {}", numeroInscripcion);
        EstudianteDTO estudiante = estudianteService.obtenerEstudiantePorNumeroInscripcion(numeroInscripcion);
        return ResponseEntity.ok(estudiante);
    }

    @GetMapping("/{id}/lock")
    @Operation(summary = "Obtener estudiante con bloqueo", description = "Retorna un estudiante con bloqueo de concurrencia")
    @ApiResponse(responseCode = "200", description = "Estudiante encontrado con bloqueo",
                 content = @Content(schema = @Schema(implementation = Estudiante.class)))
    public ResponseEntity<Estudiante> getEstudianteConBloqueo(
            @Parameter(description = "ID del estudiante", example = "1", required = true)
            @PathVariable Long id) {
        logger.info("[ESTUDIANTE] Consultando con bloqueo ID {}", id);
        Estudiante estudiante = estudianteService.obtenerEstudianteConBloqueo(id);
        return ResponseEntity.ok(estudiante);
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Crear nuevo estudiante", description = "Registra un nuevo estudiante en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Estudiante creado",
                     content = @Content(schema = @Schema(implementation = EstudianteDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EstudianteDTO> crearEstudiante(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del nuevo estudiante",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EstudianteDTO.class)))
            @Valid @RequestBody EstudianteDTO estudianteDTO) {
        logger.info("[ESTUDIANTE] Creando nuevo estudiante: {}", estudianteDTO);
        EstudianteDTO nuevo = estudianteService.crearEstudiante(estudianteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Actualizar estudiante", description = "Modifica los datos de un estudiante existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudiante actualizado",
                     content = @Content(schema = @Schema(implementation = EstudianteDTO.class))),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EstudianteDTO> actualizarEstudiante(
            @Parameter(description = "ID del estudiante a actualizar", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos del estudiante actualizados",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EstudianteDTO.class)))
            @Valid @RequestBody EstudianteDTO estudianteDTO) {
        logger.info("[ESTUDIANTE] Actualizando estudiante ID {}: {}", id, estudianteDTO);
        EstudianteDTO actualizado = estudianteService.actualizarEstudiante(id, estudianteDTO);
        return ResponseEntity.ok(actualizado);
    }

    @PutMapping("/{id}/baja")
    @Transactional
    @Operation(summary = "Dar de baja estudiante", description = "Marca a un estudiante como inactivo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudiante dado de baja",
                     content = @Content(schema = @Schema(implementation = EstudianteDTO.class))),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ResponseEntity<EstudianteDTO> eliminarEstudiante(
            @Parameter(description = "ID del estudiante a dar de baja", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Confirmación para dar de baja al estudiante",
                    required = true,
                    content = @Content(schema = @Schema(implementation = EstudianteDTO.class)))
            @Valid @RequestBody EstudianteDTO estudianteDTO) {
        logger.info("[ESTUDIANTE] Dando de baja estudiante ID {}: {}", id, estudianteDTO);
        EstudianteDTO eliminado = estudianteService.eliminarEstudiante(id, estudianteDTO);
        return ResponseEntity.ok(eliminado);
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar estudiantes activos", description = "Retorna estudiantes marcados como activos")
    @ApiResponse(responseCode = "200", description = "Lista de estudiantes activos",
                 content = @Content(schema = @Schema(implementation = EstudianteDTO.class)))
    public ResponseEntity<List<EstudianteDTO>> obtenerEstudianteActivo() {
        logger.info("[ESTUDIANTE] Listando estudiantes activos");
        List<EstudianteDTO> activos = estudianteService.obtenerEstudianteActivo();
        return ResponseEntity.ok(activos);
    }
}

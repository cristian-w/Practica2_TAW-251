package com.universidad.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.io.Serializable;

/**
 * DTO que representa los datos de un estudiante para transferencia entre capas.
 * Incluye validaciones para los campos principales y datos de inscripción, estado y fechas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Objeto de transferencia de datos para estudiantes")
public class EstudianteDTO implements Serializable {

    @Schema(description = "Identificador único del estudiante", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre del estudiante", example = "Juan", required = true)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Apellido del estudiante", example = "Pérez", required = true)
    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 3, max = 50, message = "El apellido debe tener entre 3 y 50 caracteres")
    private String apellido;

    @Schema(description = "Correo electrónico del estudiante", example = "juan.perez@universidad.edu", required = true)
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no es válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String email;

    @Schema(description = "Fecha de nacimiento del estudiante (formato: yyyy-MM-dd)", example = "2000-01-15", required = true)
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    private LocalDate fechaNacimiento;

    @Schema(description = "Número de inscripción único del estudiante", example = "INS-2023-001", required = true)
    @NotBlank(message = "El número de inscripción es obligatorio")
    @Size(min = 5, max = 20, message = "El número de inscripción debe tener entre 5 y 20 caracteres")
    private String numeroInscripcion;

    @Schema(description = "Estado actual del estudiante", example = "activo", allowableValues = {"activo", "inactivo"}, required = true)
    @NotBlank(message = "El estado es obligatorio")
    @Size(min = 3, max = 20, message = "El estado debe tener entre 3 y 20 caracteres")
    @Pattern(regexp = "^(activo|inactivo)$", message = "El estado debe ser 'activo' o 'inactivo'")
    private String estado;

    @Schema(description = "Usuario que registró al estudiante", example = "admin", required = true)
    @NotBlank(message = "El usuario de alta es obligatorio")
    @Size(min = 3, max = 50, message = "El usuario de alta debe tener entre 3 y 50 caracteres")
    private String usuarioAlta;

    @Schema(description = "Fecha de registro del estudiante (formato: yyyy-MM-dd)", example = "2023-01-10", required = true)
    @NotNull(message = "La fecha de alta es obligatoria")
    @PastOrPresent(message = "La fecha de alta debe ser anterior o igual a la fecha actual")
    private LocalDate fechaAlta;

    @Schema(description = "Usuario que realizó la última modificación", example = "modificador")
    @Size(min = 3, max = 50, message = "El usuario de modificacion debe tener entre 3 y 50 caracteres")
    private String usuarioModificacion;

    @Schema(description = "Fecha de última modificación (formato: yyyy-MM-dd)", example = "2023-05-20")
    @FutureOrPresent(message = "La fecha de modificacion debe ser mayor o igual a la fecha actual")
    private LocalDate fechaModificacion;

    @Schema(description = "Usuario que dio de baja al estudiante", example = "admin")
    @Size(min = 3, max = 50, message = "El usuario de baja debe tener entre 3 y 50 caracteres")
    private String usuarioBaja;

    @Schema(description = "Fecha de baja del estudiante (formato: yyyy-MM-dd)", example = "2023-12-31")
    @FutureOrPresent(message = "La fecha de baja debe ser mayor o igual a la fecha actual")
    private LocalDate fechaBaja;

    @Schema(description = "Motivo de baja del estudiante", example = "renuncia", allowableValues = {"renuncia", "desercion", "traslado"})
    @Size(min = 3, max = 100, message = "El motivo de baja debe tener entre 3 y 100 caracteres")
    @Pattern(regexp = "^(renuncia|desercion|traslado)$", message = "El motivo de baja debe ser 'renuncia', 'desercion' o 'traslado'")
    private String motivoBaja;
}
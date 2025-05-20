package com.universidad.dto;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para la asignación de docentes a materias")
public class AsignacionDocenteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID de la asignación", example = "10")
    private Long id;

    @NotNull(message = "El ID del docente es obligatorio")
    @Positive(message = "El ID del docente debe ser un número positivo")
    @Schema(description = "ID del docente asignado", example = "3", required = true)
    private Long id_docente;

    @NotNull(message = "El ID de la materia es obligatorio")
    @Positive(message = "El ID de la materia debe ser un número positivo")
    @Schema(description = "ID de la materia a asignar", example = "7", required = true)
    private Long id_materia;

    @NotBlank(message = "El horario no puede estar vacío")
    @Size(min = 5, max = 50, message = "El horario debe tener entre 5 y 50 caracteres")
    @Pattern(
        regexp = "^[A-Za-z]+(?:-[A-Za-z]+)?\\s\\d{2}:\\d{2}-\\d{2}:\\d{2}$",
        message = "Formato de horario inválido. Ejemplo válido: Lunes-Miércoles 12:00-14:00"
    )
    @Schema(
        description = "Horario de clase (formato: 'Lunes-Miércoles 12:00-14:00')",
        example = "Martes-Jueves 08:00-10:00",
        required = true
    )
    private String horario;

    @NotBlank(message = "El paralelo no puede estar vacío")
    @Size(min = 1, max = 10, message = "El paralelo debe tener entre 1 y 10 caracteres")
    @Pattern(
        regexp = "^[A-Za-z]\\d{0,2}$",
        message = "El paralelo debe comenzar con una letra seguida de hasta 2 números (ej: A, B1, C12)"
    )
    @Schema(description = "Paralelo asignado al curso", example = "B2", required = true)
    private String paralelo;

    @PastOrPresent(message = "La fecha de asignación no puede ser futura")
    @Schema(description = "Fecha en la que se asignó al docente", example = "2024-05-10")
    private LocalDate fechaAsignacion;

    @NotBlank(message = "El período académico es obligatorio")
    @Pattern(
        regexp = "^\\d{4}-[1-2]$",
        message = "El período académico debe tener formato YYYY-1 o YYYY-2 (ej: 2023-1, 2023-2)"
    )
    @Schema(description = "Período académico (formato: YYYY-1 o YYYY-2)", example = "2025-1", required = true)
    private String periodoAcademico;
}

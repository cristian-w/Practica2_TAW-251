package com.universidad.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO que representa una inscripción de estudiante a materia")
public class InscripcionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID único de la inscripción", 
            example = "1", 
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del estudiante que se inscribe", 
            example = "101", 
            required = true)
    @NotNull(message = "El ID del estudiante es obligatorio")
    @Positive(message = "El ID del estudiante debe ser un número positivo")
    private Long id_estudiante;

    @Schema(description = "ID de la materia a la que se inscribe", 
            example = "205", 
            required = true)
    @NotNull(message = "El ID de la materia es obligatorio")
    @Positive(message = "El ID de la materia debe ser un número positivo")
    private Long id_materia;

    @Schema(description = "Fecha de inscripción (formato: yyyy-MM-dd)", 
            example = "2023-05-15", 
            required = false)
    @PastOrPresent(message = "La fecha de inscripción no puede ser futura")
    private LocalDate fechaInscripcion;

    @Schema(description = "Grupo o paralelo al que se inscribe", 
            example = "A1", 
            required = true,
            pattern = "^[A-Za-z0-9]+$",
            minLength = 1,
            maxLength = 10)
    @NotBlank(message = "El paralelo no puede estar vacío")
    @Size(min = 1, max = 10, message = "El paralelo debe tener entre 1 y 10 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "El paralelo solo puede contener letras y números")
    private String paralelo;
}
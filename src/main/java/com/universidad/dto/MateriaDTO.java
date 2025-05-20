package com.universidad.dto;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para representar una materia con sus atributos y prerequisitos")
public class MateriaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID de la materia", example = "1")
    private Long id;

    @NotBlank(message = "El nombre de la materia es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre de la materia debe tener entre 3 y 100 caracteres")
    @Schema(description = "Nombre de la materia", example = "Programación Orientada a Objetos", required = true)
    private String nombreMateria;

    @NotBlank(message = "El código único es obligatorio")
    @Pattern(regexp = "^[A-Z]{3}\\d{3}$", message = "El código único debe tener el formato AAA999 (3 letras mayúsculas seguidas de 3 dígitos)")
    @Schema(description = "Código único de la materia (ej: PRO101)", example = "MAT101", required = true)
    private String codigoUnico;

    @NotNull(message = "El número de créditos es obligatorio")
    @Min(value = 1, message = "La materia debe tener al menos 1 crédito")
    @Max(value = 10, message = "La materia no puede tener más de 10 créditos")
    @Schema(description = "Cantidad de créditos asignados a la materia", example = "4", required = true)
    private Integer creditos;

    @Schema(
        description = "Lista de IDs de materias que son prerequisitos para esta materia",
        example = "[2, 3]"
    )
    private List<@NotNull(message = "El ID del prerequisito no puede ser nulo") @Positive(message = "El ID del prerequisito debe ser positivo") Long> prerequisitos;

    @Schema(
        description = "Lista de IDs de materias para las que esta materia es prerequisito",
        example = "[5, 6]"
    )
    private List<@NotNull(message = "El ID de la materia dependiente no puede ser nulo") @Positive(message = "El ID de la materia dependiente debe ser positivo") Long> esPrerequisitoDe;
}

package com.universidad.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "asignacion_docente")
public class AsignacionDocente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El docente no puede ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_docente", nullable = false)
    private Docente docente;

    @NotNull(message = "La materia no puede ser nula")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_materia", nullable = false)
    private Materia materia;

    @NotBlank(message = "El horario no puede estar vacío")
    @Size(min = 5, max = 50, message = "El horario debe tener entre 5 y 50 caracteres")
    @Pattern(regexp = "^[A-Za-z]+(?:-[A-Za-z]+)?\\s\\d{2}:\\d{2}-\\d{2}:\\d{2}$",message = "Formato de horario inválido. Ejemplo: Lunes-Miércoles 12:00-14:00")
    @Column(name = "horario", nullable = false)
    private String horario;

    @NotBlank(message = "El paralelo no puede estar vacío")
    @Size(min = 1, max = 10, message = "El paralelo debe tener entre 1 y 10 caracteres")
    @Pattern(regexp = "^[A-Za-z]\\d{0,2}$",message = "El paralelo debe comenzar con una letra seguida de hasta 2 números (ej: A, B1, C12)")
    @Column(name = "paralelo", nullable = false)
    private String paralelo;

    @PastOrPresent(message = "La fecha de asignación no puede ser futura")
    @Column(name = "fecha_asignacion")
    private LocalDate fechaAsignacion;

    @NotBlank(message = "El período académico es obligatorio")
    @Pattern(regexp = "^\\d{4}-[1-2]$",message = "El período académico debe tener formato YYYY-1 o YYYY-2 (ej: 2023-1, 2023-2)")
    @Column(name = "periodo_academico", nullable = false)
    private String periodoAcademico;
}

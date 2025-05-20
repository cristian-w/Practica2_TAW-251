package com.universidad.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "inscripcion")
public class Inscripcion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante", nullable = false)
    @NotNull(message = "El estudiante no puede ser nulo")
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_materia", nullable = false)
    @NotNull(message = "La materia no puede ser nula")
    private Materia materia;

    @Column(name = "fecha_inscripcion")    
    @PastOrPresent(message = "La fecha de inscripción debe ser actual o pasada")
    private LocalDate fechaInscripcion;

    @Column(name = "paralelo", nullable = false, length = 10)
    @NotBlank(message = "El paralelo no puede estar vacío")
    @Size(min = 1, max = 10, message = "El paralelo debe tener entre 1 y 10 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "El paralelo solo puede contener letras y números")
    private String paralelo;
}
package com.pe.cine_culturav3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "pelicula")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 1, max = 200, message = "El título debe tener entre 1 y 200 caracteres")
    @Column(nullable = false, length = 200)
    private String titulo;

    @Size(max = 500, message = "La URL de imagen no puede superar los 500 caracteres")
    @Column(length = 500)
    private String imagen;

    @Size(max = 500, message = "La URL de portada no puede superar los 500 caracteres")
    @Column(length = 500)
    private String portada;

    @NotBlank(message = "El enlace es obligatorio")
    @Size(max = 1000, message = "El enlace no puede superar los 1000 caracteres")
    @Column(nullable = false, length = 1000)
    private String enlace;

    @NotNull(message = "El año de publicación es obligatorio")
    @Min(value = 1900, message = "El año debe ser mayor a 1900")
    @Max(value = 2030, message = "El año no puede ser mayor a 2030")
    @Column(name = "anio_pub", nullable = false)
    private Integer anioPub;

    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración debe ser mayor a 0 minutos")
    @Max(value = 600, message = "La duración no puede superar los 600 minutos")
    @Column(nullable = false)
    private Integer duracion;

    @Column(columnDefinition = "TEXT")
    private String actores;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @DecimalMin(value = "1.0", message = "La calificación debe ser mínimo 1.0")
    @DecimalMax(value = "5.0", message = "La calificación debe ser máximo 5.0")
    @Digits(integer = 1, fraction = 1, message = "La calificación debe tener formato X.X")
    @Column(precision = 2, scale = 1)
    private BigDecimal calificacion;

    @NotNull(message = "El usuario es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @NotNull(message = "La categoría es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;
}

package com.pe.cine_culturav3.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "tipo_suscripcion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoSuscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del tipo de suscripción es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @NotNull(message = "La duración en meses es obligatoria")
    @Min(value = 1, message = "La duración debe ser mínimo 1 mes")
    @Max(value = 12, message = "La duración no puede superar los 12 meses")
    @Column(name = "duracion_meses", nullable = false)
    private Integer duracionMeses;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @DecimalMax(value = "999.99", message = "El precio no puede superar los 999.99")
    @Digits(integer = 6, fraction = 2, message = "El precio debe tener formato válido")
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal precio;

    @Column(columnDefinition = "TEXT")
    private String descripcion;
}

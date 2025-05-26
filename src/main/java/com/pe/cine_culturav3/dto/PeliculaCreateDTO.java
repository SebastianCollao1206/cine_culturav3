package com.pe.cine_culturav3.dto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeliculaCreateDTO {
    @NotBlank(message = "El título es obligatorio")
    @Size(min = 1, max = 200, message = "El título debe tener entre 1 y 200 caracteres")
    private String titulo;

    @Size(max = 500, message = "La URL de imagen no puede superar los 500 caracteres")
    private String imagen;

    @Size(max = 500, message = "La URL de portada no puede superar los 500 caracteres")
    private String portada;

    @NotBlank(message = "El enlace es obligatorio")
    @Size(max = 1000, message = "El enlace no puede superar los 1000 caracteres")
    private String enlace;

    @NotNull(message = "El año de publicación es obligatorio")
    @Min(value = 1900, message = "El año debe ser mayor a 1900")
    @Max(value = 2030, message = "El año no puede ser mayor a 2030")
    private Integer anioPub;

    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración debe ser mayor a 0 minutos")
    @Max(value = 600, message = "La duración no puede superar los 600 minutos")
    private Integer duracion;

    private String actores;

    private String descripcion;

    @DecimalMin(value = "1.0", message = "La calificación debe ser mínimo 1.0")
    @DecimalMax(value = "5.0", message = "La calificación debe ser máximo 5.0")
    @Digits(integer = 1, fraction = 1, message = "La calificación debe tener formato X.X")
    private BigDecimal calificacion;

    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;
}

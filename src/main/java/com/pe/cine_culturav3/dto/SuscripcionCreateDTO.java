package com.pe.cine_culturav3.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuscripcionCreateDTO {
    @NotNull(message = "El método de pago es obligatorio")
    private Long metodoPagoId;

    @NotNull(message = "El tipo de suscripción es obligatorio")
    private Long tipoSuscripcionId;
}

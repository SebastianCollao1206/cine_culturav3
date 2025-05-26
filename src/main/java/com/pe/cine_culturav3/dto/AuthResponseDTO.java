package com.pe.cine_culturav3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDTO {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private LocalDateTime fechaCreacion;
    private String token;
    private String message;

    public AuthResponseDTO(Long id, String username, String email, List<String> roles, LocalDateTime fechaCreacion, String message) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.fechaCreacion = fechaCreacion;
        this.message = message;
    }
}

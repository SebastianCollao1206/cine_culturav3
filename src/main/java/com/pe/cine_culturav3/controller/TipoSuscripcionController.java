package com.pe.cine_culturav3.controller;

import com.pe.cine_culturav3.model.TipoSuscripcion;
import com.pe.cine_culturav3.service.TipoSuscripcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-suscripcion")
@RequiredArgsConstructor
public class TipoSuscripcionController {
    private final TipoSuscripcionService tipoSuscripcionService;

    @GetMapping
    public ResponseEntity<List<TipoSuscripcion>> listarTodos() {
        return ResponseEntity.ok(tipoSuscripcionService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoSuscripcion> buscarPorId(@PathVariable Long id) {
        return tipoSuscripcionService.buscarPorId(id)
                .map(tipo -> ResponseEntity.ok(tipo))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TipoSuscripcion> crear(@Valid @RequestBody TipoSuscripcion tipoSuscripcion) {
        try {
            TipoSuscripcion nuevoTipo = tipoSuscripcionService.crear(tipoSuscripcion);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoTipo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TipoSuscripcion> actualizar(@PathVariable Long id, @Valid @RequestBody TipoSuscripcion tipoSuscripcion) {
        try {
            TipoSuscripcion tipoActualizado = tipoSuscripcionService.actualizar(id, tipoSuscripcion);
            return ResponseEntity.ok(tipoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/parcial/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> actualizarParcial(@PathVariable Long id,
                                               @RequestParam(required = false) String nombre,
                                               @RequestParam(required = false) Integer duracionMeses,
                                               @RequestParam(required = false) BigDecimal precio,
                                               @RequestParam(required = false) String descripcion) {
        try {
            TipoSuscripcion tipoActualizado = tipoSuscripcionService.actualizarParcial(
                    id, nombre, duracionMeses, precio, descripcion);
            return ResponseEntity.ok(tipoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

package com.pe.cine_culturav3.controller;

import com.pe.cine_culturav3.dto.SuscripcionCreateDTO;
import com.pe.cine_culturav3.model.EstadoSuscripcion;
import com.pe.cine_culturav3.model.Suscripcion;
import com.pe.cine_culturav3.service.SuscripcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suscripciones")
@RequiredArgsConstructor
public class SuscripcionController {
    private final SuscripcionService suscripcionService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Suscripcion>> listarTodas() {
        return ResponseEntity.ok(suscripcionService.listarTodas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Suscripcion> buscarPorId(@PathVariable Long id) {
        return suscripcionService.buscarPorId(id)
                .map(suscripcion -> ResponseEntity.ok(suscripcion))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Suscripcion>> buscarPorEstado(@PathVariable EstadoSuscripcion estado) {
        return ResponseEntity.ok(suscripcionService.buscarPorEstado(estado));
    }

    @GetMapping("/usuario/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<Suscripcion>> buscarPorUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(suscripcionService.buscarPorUsuario(userId));
    }

    @GetMapping("/usuario/{userId}/activa")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public ResponseEntity<Suscripcion> buscarSuscripcionActivaPorUsuario(@PathVariable Long userId) {
        return suscripcionService.buscarSuscripcionActivaPorUsuario(userId)
                .map(suscripcion -> ResponseEntity.ok(suscripcion))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Suscripcion> crear(@Valid @RequestBody SuscripcionCreateDTO suscripcionDTO) {
        try {
            Suscripcion nuevaSuscripcion = suscripcionService.crear(suscripcionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaSuscripcion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/cancelar-mi-suscripcion")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Suscripcion> cancelarMiSuscripcion() {
        try {
            Suscripcion suscripcionCancelada = suscripcionService.cancelarMiSuscripcion();
            return ResponseEntity.ok(suscripcionCancelada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/mis-suscripciones")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<Suscripcion>> misSuscripciones() {
        return ResponseEntity.ok(suscripcionService.misSuscripciones());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Suscripcion> actualizar(@PathVariable Long id, @RequestBody Suscripcion suscripcion) {
        try {
            Suscripcion suscripcionActualizada = suscripcionService.actualizar(id, suscripcion);
            return ResponseEntity.ok(suscripcionActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

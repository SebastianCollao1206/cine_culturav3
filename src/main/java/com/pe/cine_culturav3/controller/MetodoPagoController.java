package com.pe.cine_culturav3.controller;

import com.pe.cine_culturav3.model.MetodoPago;
import com.pe.cine_culturav3.service.MetodoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/metodos-pago")
@RequiredArgsConstructor
public class MetodoPagoController {
    private final MetodoPagoService metodoPagoService;

    @GetMapping
    public ResponseEntity<List<MetodoPago>> listarTodos() {
        return ResponseEntity.ok(metodoPagoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodoPago> buscarPorId(@PathVariable Long id) {
        return metodoPagoService.buscarPorId(id)
                .map(metodoPago -> ResponseEntity.ok(metodoPago))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<MetodoPago> buscarPorNombre(@PathVariable String nombre) {
        return metodoPagoService.buscarPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MetodoPago> crear(@Valid @RequestBody MetodoPago metodoPago) {
        try {
            MetodoPago nuevoMetodo = metodoPagoService.crear(metodoPago);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMetodo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<MetodoPago> actualizar(@PathVariable Long id, @Valid @RequestBody MetodoPago metodoPago) {
        try {
            MetodoPago metodoActualizado = metodoPagoService.actualizar(id, metodoPago);
            return ResponseEntity.ok(metodoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/parcial/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> actualizarParcial(@PathVariable Long id,
                                               @RequestParam(required = false) String nombre) {
        try {
            MetodoPago metodoActualizado = metodoPagoService.actualizarParcial(id, nombre);
            return ResponseEntity.ok(metodoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

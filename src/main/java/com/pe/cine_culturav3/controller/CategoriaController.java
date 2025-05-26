package com.pe.cine_culturav3.controller;

import com.pe.cine_culturav3.model.Categoria;
import com.pe.cine_culturav3.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoriaController {
    private final CategoriaService categoriaService;

    // ENDPOINTS DE LECTURA - LIBRES
    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodas() {
        List<Categoria> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaService.buscarPorId(id);
        return categoria.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Categoria> buscarPorNombre(@PathVariable String nombre) {
        Optional<Categoria> categoria = categoriaService.buscarPorNombre(nombre);
        return categoria.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{enabled}")
    public ResponseEntity<List<Categoria>> listarPorEstado(@PathVariable Boolean enabled) {
        List<Categoria> categorias = categoriaService.listarPorEstado(enabled);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/activas")
    public ResponseEntity<List<Categoria>> listarActivasOrdenadas() {
        List<Categoria> categorias = categoriaService.listarActivasOrdenadas();
        return ResponseEntity.ok(categorias);
    }

    // ENDPOINTS DE ESCRITURA - REQUIEREN AUTORIZACIÓN
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Categoria> crear(@Valid @RequestBody Categoria categoria) {
        try {
            Categoria nuevaCategoria = categoriaService.crear(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id, @Valid @RequestBody Categoria categoria) {
        try {
            categoria.setId(id);
            Categoria categoriaActualizada = categoriaService.actualizar(categoria);
            return ResponseEntity.ok(categoriaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Categoria> cambiarEstado(@PathVariable Long id, @RequestParam Boolean enabled) {
        try {
            Categoria categoria = categoriaService.cambiarEstado(id, enabled);
            return ResponseEntity.ok(categoria);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

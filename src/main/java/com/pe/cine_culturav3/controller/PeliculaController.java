package com.pe.cine_culturav3.controller;

import com.pe.cine_culturav3.dto.PeliculaCreateDTO;
import com.pe.cine_culturav3.dto.PeliculaUpdateDTO;
import com.pe.cine_culturav3.model.Pelicula;
import com.pe.cine_culturav3.service.PeliculaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/peliculas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PeliculaController {
    private final PeliculaService peliculaService;

    // ENDPOINTS DE LECTURA - LIBRES
    @GetMapping
    public ResponseEntity<List<Pelicula>> listarTodas() {
        List<Pelicula> peliculas = peliculaService.listarTodas();
        return ResponseEntity.ok(peliculas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pelicula> buscarPorId(@PathVariable Long id) {
        Optional<Pelicula> pelicula = peliculaService.buscarPorId(id);
        return pelicula.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<Pelicula>> listarPorCategoria(@PathVariable Long categoriaId) {
        List<Pelicula> peliculas = peliculaService.listarPorCategoria(categoriaId);
        return ResponseEntity.ok(peliculas);
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<Pelicula>> listarPorUsuario(@PathVariable Long userId) {
        List<Pelicula> peliculas = peliculaService.listarPorUsuario(userId);
        return ResponseEntity.ok(peliculas);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Pelicula>> buscarPorTitulo(@RequestParam String titulo) {
        List<Pelicula> peliculas = peliculaService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(peliculas);
    }

    @GetMapping("/anio/{anio}")
    public ResponseEntity<List<Pelicula>> listarPorAnio(@PathVariable Integer anio) {
        List<Pelicula> peliculas = peliculaService.listarPorAnioOrdenadas(anio);
        return ResponseEntity.ok(peliculas);
    }

    @GetMapping("/ordenadas")
    public ResponseEntity<List<Pelicula>> listarTodasOrdenadas() {
        List<Pelicula> peliculas = peliculaService.listarTodasOrdenadas();
        return ResponseEntity.ok(peliculas);
    }

    @GetMapping("/categorias-activas")
    public ResponseEntity<List<Pelicula>> listarConCategoriasActivas() {
        List<Pelicula> peliculas = peliculaService.listarConCategoriasActivas();
        return ResponseEntity.ok(peliculas);
    }

    @GetMapping("/usuarios-activos")
    public ResponseEntity<List<Pelicula>> listarConUsuariosActivos() {
        List<Pelicula> peliculas = peliculaService.listarConUsuariosActivos();
        return ResponseEntity.ok(peliculas);
    }

    // ENDPOINTS DE ESCRITURA - REQUIEREN AUTORIZACIÓN
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> crear(@Valid @RequestBody PeliculaCreateDTO peliculaDTO) {
        try {
            Pelicula nuevaPelicula = peliculaService.crear(peliculaDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPelicula);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @Valid @RequestBody PeliculaUpdateDTO peliculaDTO) {
        try {
            Pelicula peliculaActualizada = peliculaService.actualizar(id, peliculaDTO);
            return ResponseEntity.ok(peliculaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno del servidor");
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<Pelicula> actualizarParcial(@PathVariable Long id,
                                                      @RequestBody Pelicula datosActualizacion,
                                                      @RequestParam(required = false) Long userId,
                                                      @RequestParam(required = false) Long categoriaId) {
        try {
            Pelicula peliculaActualizada = peliculaService.actualizarParcial(id, datosActualizacion, userId, categoriaId);
            return ResponseEntity.ok(peliculaActualizada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

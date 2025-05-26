package com.pe.cine_culturav3.service;

import com.pe.cine_culturav3.model.Categoria;
import com.pe.cine_culturav3.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    // MÉTODOS DE LECTURA - LIBRES (sin autorización)
    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Categoria> buscarPorNombre(String nombre) {
        return categoriaRepository.findByNombre(nombre);
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarPorEstado(Boolean enabled) {
        return categoriaRepository.findByEnabled(enabled);
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarActivasOrdenadas() {
        return categoriaRepository.findByEnabledOrderByNombre(true);
    }

    // MÉTODOS DE ESCRITURA - REQUIEREN AUTORIZACIÓN
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public Categoria crear(Categoria categoria) {
        if (categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new IllegalArgumentException("El nombre de la categoría ya existe");
        }
        return categoriaRepository.save(categoria);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public Categoria actualizar(Categoria categoria) {
        Categoria existente = categoriaRepository.findById(categoria.getId())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        if (!existente.getNombre().equals(categoria.getNombre()) &&
                categoriaRepository.existsByNombre(categoria.getNombre())) {
            throw new IllegalArgumentException("El nombre de la categoría ya existe");
        }

        existente.setNombre(categoria.getNombre());
        existente.setEnabled(categoria.getEnabled());

        return categoriaRepository.save(existente);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public Categoria cambiarEstado(Long id, Boolean enabled) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
        categoria.setEnabled(enabled);
        return categoriaRepository.save(categoria);
    }
}

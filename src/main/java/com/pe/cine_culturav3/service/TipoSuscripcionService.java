package com.pe.cine_culturav3.service;

import com.pe.cine_culturav3.model.TipoSuscripcion;
import com.pe.cine_culturav3.repository.TipoSuscripcionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TipoSuscripcionService {
    private final TipoSuscripcionRepository tipoSuscripcionRepository;

    @Transactional(readOnly = true)
    public List<TipoSuscripcion> listarTodos() {
        return tipoSuscripcionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<TipoSuscripcion> buscarPorId(Long id) {
        return tipoSuscripcionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<TipoSuscripcion> buscarPorNombre(String nombre) {
        return tipoSuscripcionRepository.findByNombre(nombre);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public TipoSuscripcion crear(TipoSuscripcion tipoSuscripcion) {
        if (tipoSuscripcionRepository.existsByNombre(tipoSuscripcion.getNombre())) {
            throw new RuntimeException("Ya existe un tipo de suscripción con este nombre");
        }
        return tipoSuscripcionRepository.save(tipoSuscripcion);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public TipoSuscripcion actualizar(Long id, TipoSuscripcion tipoSuscripcion) {
        return tipoSuscripcionRepository.findById(id)
                .map(existente -> {
                    if (!existente.getNombre().equals(tipoSuscripcion.getNombre()) &&
                            tipoSuscripcionRepository.existsByNombre(tipoSuscripcion.getNombre())) {
                        throw new RuntimeException("Ya existe un tipo de suscripción con este nombre");
                    }
                    existente.setNombre(tipoSuscripcion.getNombre());
                    existente.setDuracionMeses(tipoSuscripcion.getDuracionMeses());
                    existente.setPrecio(tipoSuscripcion.getPrecio());
                    existente.setDescripcion(tipoSuscripcion.getDescripcion());
                    return tipoSuscripcionRepository.save(existente);
                })
                .orElseThrow(() -> new RuntimeException("Tipo de suscripción no encontrado"));
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public TipoSuscripcion actualizarParcial(Long id, String nombre, Integer duracionMeses,
                                             BigDecimal precio, String descripcion) {
        TipoSuscripcion existente = tipoSuscripcionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de suscripción no encontrado"));

        if (nombre != null && !nombre.trim().isEmpty()) {
            existente.setNombre(nombre);
        }
        if (duracionMeses != null) {
            existente.setDuracionMeses(duracionMeses);
        }
        if (precio != null) {
            existente.setPrecio(precio);
        }
        if (descripcion != null) {
            existente.setDescripcion(descripcion);
        }

        return tipoSuscripcionRepository.save(existente);
    }

    public boolean existeTipoSuscripcion(Long id) {
        return tipoSuscripcionRepository.existsById(id);
    }
}

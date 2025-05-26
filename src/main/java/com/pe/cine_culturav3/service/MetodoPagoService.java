package com.pe.cine_culturav3.service;

import com.pe.cine_culturav3.model.MetodoPago;
import com.pe.cine_culturav3.repository.MetodoPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MetodoPagoService {
    private final MetodoPagoRepository metodoPagoRepository;

    @Transactional(readOnly = true)
    public List<MetodoPago> listarTodos() {
        return metodoPagoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<MetodoPago> buscarPorId(Long id) {
        return metodoPagoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<MetodoPago> buscarPorNombre(String nombre) {
        return metodoPagoRepository.findByNombre(nombre);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MetodoPago crear(MetodoPago metodoPago) {
        if (metodoPagoRepository.existsByNombre(metodoPago.getNombre())) {
            throw new RuntimeException("Ya existe un método de pago con este nombre");
        }
        return metodoPagoRepository.save(metodoPago);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MetodoPago actualizar(Long id, MetodoPago metodoPago) {
        return metodoPagoRepository.findById(id)
                .map(existente -> {
                    if (!existente.getNombre().equals(metodoPago.getNombre()) &&
                            metodoPagoRepository.existsByNombre(metodoPago.getNombre())) {
                        throw new RuntimeException("Ya existe un método de pago con este nombre");
                    }
                    existente.setNombre(metodoPago.getNombre());
                    return metodoPagoRepository.save(existente);
                })
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MetodoPago actualizarParcial(Long id, String nombre) {
        MetodoPago existente = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Método de pago no encontrado"));

        if (nombre != null && !nombre.trim().isEmpty()) {
            if (!existente.getNombre().equals(nombre) && metodoPagoRepository.existsByNombre(nombre)) {
                throw new IllegalArgumentException("Ya existe un método de pago con ese nombre");
            }
            existente.setNombre(nombre);
        }

        return metodoPagoRepository.save(existente);
    }

    public boolean existeMetodoPago(Long id) {
        return metodoPagoRepository.existsById(id);
    }
}

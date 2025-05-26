package com.pe.cine_culturav3.service;

import com.pe.cine_culturav3.dto.SuscripcionCreateDTO;
import com.pe.cine_culturav3.model.*;
import com.pe.cine_culturav3.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuscripcionService {
    private final SuscripcionRepository suscripcionRepository;
    private final UserRepository userRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final TipoSuscripcionRepository tipoSuscripcionRepository;
    private final AuthService authService;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Suscripcion> listarTodas() {
        return suscripcionRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Optional<Suscripcion> buscarPorId(Long id) {
        return suscripcionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Suscripcion> buscarPorEstado(EstadoSuscripcion estado) {
        return suscripcionRepository.findByEstado(estado);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public List<Suscripcion> buscarPorUsuario(Long userId) {
        return suscripcionRepository.encontrarPorUsuario(userId);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public Optional<Suscripcion> buscarSuscripcionActivaPorUsuario(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return suscripcionRepository.findByUserAndEstado(user, EstadoSuscripcion.ACTIVA);
    }

    // TRANSACCIÓN: Afecta User, MetodoPago, TipoSuscripcion y Suscripcion
    @Transactional
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public Suscripcion crear(SuscripcionCreateDTO suscripcionDTO) {
        // Obtener el usuario autenticado actual
        User usuarioAutenticado = authService.getCurrentUser();

        MetodoPago metodoPago = metodoPagoRepository.findById(suscripcionDTO.getMetodoPagoId())
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado"));

        TipoSuscripcion tipoSuscripcion = tipoSuscripcionRepository.findById(suscripcionDTO.getTipoSuscripcionId())
                .orElseThrow(() -> new RuntimeException("Tipo de suscripción no encontrado"));

        // Verificar si ya tiene una suscripción activa
        Optional<Suscripcion> suscripcionActiva = suscripcionRepository.findByUserAndEstado(usuarioAutenticado, EstadoSuscripcion.ACTIVA);
        if (suscripcionActiva.isPresent()) {
            throw new RuntimeException("Ya tienes una suscripción activa");
        }

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setUser(usuarioAutenticado);
        suscripcion.setMetodoPago(metodoPago);
        suscripcion.setTipoSuscripcion(tipoSuscripcion);
        suscripcion.setEstado(EstadoSuscripcion.ACTIVA);

        return suscripcionRepository.save(suscripcion);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public Suscripcion cancelarMiSuscripcion() {
        User usuarioAutenticado = authService.getCurrentUser();

        return suscripcionRepository.findByUserAndEstado(usuarioAutenticado, EstadoSuscripcion.ACTIVA)
                .map(suscripcion -> {
                    suscripcion.setEstado(EstadoSuscripcion.CANCELADA);
                    return suscripcionRepository.save(suscripcion);
                })
                .orElseThrow(() -> new RuntimeException("No tienes una suscripción activa para cancelar"));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public List<Suscripcion> misSuscripciones() {
        User usuarioAutenticado = authService.getCurrentUser();
        return suscripcionRepository.encontrarPorUsuario(usuarioAutenticado.getId());
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Suscripcion actualizar(Long id, Suscripcion suscripcion) {
        return suscripcionRepository.findById(id)
                .map(existente -> {
                    existente.setFechaInicio(suscripcion.getFechaInicio());
                    existente.setFechaFin(suscripcion.getFechaFin());
                    existente.setEstado(suscripcion.getEstado());
                    return suscripcionRepository.save(existente);
                })
                .orElseThrow(() -> new RuntimeException("Suscripción no encontrada"));
    }
}

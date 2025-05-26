package com.pe.cine_culturav3.repository;

import com.pe.cine_culturav3.model.TipoSuscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoSuscripcionRepository extends JpaRepository<TipoSuscripcion, Long> {
    Optional<TipoSuscripcion> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}

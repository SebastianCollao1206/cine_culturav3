package com.pe.cine_culturav3.repository;

import com.pe.cine_culturav3.model.EstadoSuscripcion;
import com.pe.cine_culturav3.model.Suscripcion;
import com.pe.cine_culturav3.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuscripcionRepository extends JpaRepository<Suscripcion, Long> {
    List<Suscripcion> findByEstado(EstadoSuscripcion estado);
    List<Suscripcion> findByUser(User user);
    Optional<Suscripcion> findByUserAndEstado(User user, EstadoSuscripcion estado);

    @Query("SELECT s FROM Suscripcion s WHERE s.user.id = :userId")
    List<Suscripcion> encontrarPorUsuario(@Param("userId") Long userId);
}

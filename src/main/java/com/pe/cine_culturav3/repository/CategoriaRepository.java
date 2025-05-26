package com.pe.cine_culturav3.repository;

import com.pe.cine_culturav3.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    List<Categoria> findByEnabled(Boolean enabled);

    @Query("SELECT c FROM Categoria c WHERE c.enabled = :enabled ORDER BY c.nombre")
    List<Categoria> findByEnabledOrderByNombre(@Param("enabled") Boolean enabled);
}

package com.pe.cine_culturav3.repository;

import com.pe.cine_culturav3.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
    List<Pelicula> findByCategoriaId(Long categoriaId);
    List<Pelicula> findByUserId(Long userId);

    @Query("SELECT p FROM Pelicula p WHERE p.titulo LIKE %:titulo%")
    List<Pelicula> findByTituloContaining(@Param("titulo") String titulo);

    @Query("SELECT p FROM Pelicula p WHERE p.anioPub = :anio ORDER BY p.calificacion DESC")
    List<Pelicula> findByAnioOrderByCalificacionDesc(@Param("anio") Integer anio);

    @Query("SELECT p FROM Pelicula p ORDER BY p.calificacion DESC")
    List<Pelicula> findAllOrderByCalificacionDesc();

    @Query("SELECT p FROM Pelicula p WHERE p.categoria.enabled = true")
    List<Pelicula> findByCategoriasActivas();

    @Query("SELECT p FROM Pelicula p WHERE p.user.enabled = true")
    List<Pelicula> findByUsuariosActivos();
}

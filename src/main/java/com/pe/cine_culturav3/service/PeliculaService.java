package com.pe.cine_culturav3.service;

import com.pe.cine_culturav3.dto.PeliculaCreateDTO;
import com.pe.cine_culturav3.dto.PeliculaUpdateDTO;
import com.pe.cine_culturav3.model.Pelicula;
import com.pe.cine_culturav3.model.User;
import com.pe.cine_culturav3.model.Categoria;
import com.pe.cine_culturav3.repository.PeliculaRepository;
import com.pe.cine_culturav3.repository.UserRepository;
import com.pe.cine_culturav3.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PeliculaService {
    private final PeliculaRepository peliculaRepository;
    private final UserRepository userRepository;
    private final CategoriaRepository categoriaRepository;
    private final AuthService authService;

    // MÉTODOS DE LECTURA - LIBRES (sin autorización)
    @Transactional(readOnly = true)
    public List<Pelicula> listarTodas() {
        return peliculaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Pelicula> buscarPorId(Long id) {
        return peliculaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Pelicula> listarPorCategoria(Long categoriaId) {
        return peliculaRepository.findByCategoriaId(categoriaId);
    }

    @Transactional(readOnly = true)
    public List<Pelicula> listarPorUsuario(Long userId) {
        return peliculaRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Pelicula> buscarPorTitulo(String titulo) {
        return peliculaRepository.findByTituloContaining(titulo);
    }

    @Transactional(readOnly = true)
    public List<Pelicula> listarPorAnioOrdenadas(Integer anio) {
        return peliculaRepository.findByAnioOrderByCalificacionDesc(anio);
    }

    @Transactional(readOnly = true)
    public List<Pelicula> listarTodasOrdenadas() {
        return peliculaRepository.findAllOrderByCalificacionDesc();
    }

    @Transactional(readOnly = true)
    public List<Pelicula> listarConCategoriasActivas() {
        return peliculaRepository.findByCategoriasActivas();
    }

    @Transactional(readOnly = true)
    public List<Pelicula> listarConUsuariosActivos() {
        return peliculaRepository.findByUsuariosActivos();
    }

    // MÉTODOS DE ESCRITURA - REQUIEREN AUTORIZACIÓN
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public Pelicula crear(PeliculaCreateDTO peliculaDTO) {
        // Obtener el usuario autenticado actual
        User usuarioAutenticado = authService.getCurrentUser();

        Categoria categoria = categoriaRepository.findById(peliculaDTO.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        Pelicula pelicula = new Pelicula();
        pelicula.setTitulo(peliculaDTO.getTitulo());
        pelicula.setImagen(peliculaDTO.getImagen());
        pelicula.setPortada(peliculaDTO.getPortada());
        pelicula.setEnlace(peliculaDTO.getEnlace());
        pelicula.setAnioPub(peliculaDTO.getAnioPub());
        pelicula.setDuracion(peliculaDTO.getDuracion());
        pelicula.setActores(peliculaDTO.getActores());
        pelicula.setDescripcion(peliculaDTO.getDescripcion());
        pelicula.setCalificacion(peliculaDTO.getCalificacion());
        pelicula.setUser(usuarioAutenticado);
        pelicula.setCategoria(categoria);

        return peliculaRepository.save(pelicula);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public Pelicula actualizar(Long id, PeliculaUpdateDTO peliculaDTO) {
        Pelicula existente = peliculaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Película no encontrada"));

        // Verificar que el usuario autenticado sea el propietario o admin
        User usuarioAutenticado = authService.getCurrentUser();
        if (!existente.getUser().getId().equals(usuarioAutenticado.getId()) &&
                !usuarioAutenticado.getRoles().stream()
                        .anyMatch(role -> role.getName().name().equals("ROLE_ADMIN"))) {
            throw new SecurityException("No tienes permisos para actualizar esta película");
        }

        // Actualizar campos si están presentes
        if (peliculaDTO.getTitulo() != null && !peliculaDTO.getTitulo().trim().isEmpty()) {
            existente.setTitulo(peliculaDTO.getTitulo());
        }
        if (peliculaDTO.getImagen() != null) {
            existente.setImagen(peliculaDTO.getImagen());
        }
        if (peliculaDTO.getPortada() != null) {
            existente.setPortada(peliculaDTO.getPortada());
        }
        if (peliculaDTO.getEnlace() != null && !peliculaDTO.getEnlace().trim().isEmpty()) {
            existente.setEnlace(peliculaDTO.getEnlace());
        }
        if (peliculaDTO.getAnioPub() != null) {
            existente.setAnioPub(peliculaDTO.getAnioPub());
        }
        if (peliculaDTO.getDuracion() != null) {
            existente.setDuracion(peliculaDTO.getDuracion());
        }
        if (peliculaDTO.getActores() != null) {
            existente.setActores(peliculaDTO.getActores());
        }
        if (peliculaDTO.getDescripcion() != null) {
            existente.setDescripcion(peliculaDTO.getDescripcion());
        }
        if (peliculaDTO.getCalificacion() != null) {
            existente.setCalificacion(peliculaDTO.getCalificacion());
        }
        if (peliculaDTO.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(peliculaDTO.getCategoriaId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
            existente.setCategoria(categoria);
        }

        return peliculaRepository.save(existente);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public Pelicula actualizarParcial(Long id, Pelicula datosActualizacion, Long userId, Long categoriaId) {
        Pelicula existente = peliculaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Película no encontrada"));

        if (datosActualizacion.getTitulo() != null && !datosActualizacion.getTitulo().trim().isEmpty()) {
            existente.setTitulo(datosActualizacion.getTitulo());
        }

        if (datosActualizacion.getImagen() != null) {
            existente.setImagen(datosActualizacion.getImagen());
        }

        if (datosActualizacion.getPortada() != null) {
            existente.setPortada(datosActualizacion.getPortada());
        }

        if (datosActualizacion.getEnlace() != null && !datosActualizacion.getEnlace().trim().isEmpty()) {
            existente.setEnlace(datosActualizacion.getEnlace());
        }

        if (datosActualizacion.getAnioPub() != null) {
            existente.setAnioPub(datosActualizacion.getAnioPub());
        }

        if (datosActualizacion.getDuracion() != null) {
            existente.setDuracion(datosActualizacion.getDuracion());
        }

        if (datosActualizacion.getActores() != null) {
            existente.setActores(datosActualizacion.getActores());
        }

        if (datosActualizacion.getDescripcion() != null) {
            existente.setDescripcion(datosActualizacion.getDescripcion());
        }

        if (datosActualizacion.getCalificacion() != null) {
            existente.setCalificacion(datosActualizacion.getCalificacion());
        }

        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            existente.setUser(user);
        }

        if (categoriaId != null) {
            Categoria categoria = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
            existente.setCategoria(categoria);
        }

        return peliculaRepository.save(existente);
    }
}

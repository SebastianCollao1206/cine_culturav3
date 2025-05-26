package com.pe.cine_culturav3.service;

import com.pe.cine_culturav3.model.Role;
import com.pe.cine_culturav3.model.RoleName;
import com.pe.cine_culturav3.model.User;
import com.pe.cine_culturav3.repository.RoleRepository;
import com.pe.cine_culturav3.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> listarTodos() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Optional<User> buscarPorId(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Optional<User> buscarPorUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Optional<User> buscarPorEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User crear(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("El username ya existe");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("El email ya existe");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setFechaCreacion(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User actualizar(User user) {
        User existente = userRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!existente.getUsername().equals(user.getUsername()) &&
                userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("El username ya existe");
        }

        if (!existente.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("El email ya existe");
        }

        existente.setUsername(user.getUsername());
        existente.setEmail(user.getEmail());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existente.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(existente);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User actualizarParcial(Long id, String username, String email, String password, Boolean enabled) {
        User existente = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (username != null && !username.trim().isEmpty()) {
            if (!existente.getUsername().equals(username) && userRepository.existsByUsername(username)) {
                throw new IllegalArgumentException("El username ya existe");
            }
            existente.setUsername(username);
        }

        if (email != null && !email.trim().isEmpty()) {
            if (!existente.getEmail().equals(email) && userRepository.existsByEmail(email)) {
                throw new IllegalArgumentException("El email ya existe");
            }
            existente.setEmail(email);
        }

        if (password != null && !password.isEmpty()) {
            existente.setPassword(passwordEncoder.encode(password));
        }

        if (enabled != null) {
            existente.setEnabled(enabled);
        }

        return userRepository.save(existente);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User cambiarEstado(Long id, Boolean enabled) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        user.setEnabled(enabled);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> listarPorEstado(Boolean enabled) {
        return userRepository.encontrarPorEstado(enabled);
    }

    // TRANSACCIÓN: Afecta User y Role
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User asignarRol(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

        user.addRole(role);
        return userRepository.save(user);
    }
}

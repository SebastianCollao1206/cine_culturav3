package com.pe.cine_culturav3.service;

import com.pe.cine_culturav3.model.Role;
import com.pe.cine_culturav3.model.RoleName;
import com.pe.cine_culturav3.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Role> listarTodos() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Optional<Role> buscarPorId(Long id) {
        return roleRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Role> buscarPorNombre(RoleName name) {
        return roleRepository.findByName(name);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Role crear(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Role actualizar(Role role) {
        Role existente = roleRepository.findById(role.getId())
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

        existente.setName(role.getName());
        return roleRepository.save(existente);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Role actualizarParcial(Long id, RoleName name, Boolean enabled) {
        Role existente = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));

        if (name != null) {
            existente.setName(name);
        }
        if (enabled != null) {
            existente.setEnabled(enabled);
        }

        return roleRepository.save(existente);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Role cambiarEstado(Long id, Boolean enabled) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
        role.setEnabled(enabled);
        return roleRepository.save(role);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Role> listarPorEstado(Boolean enabled) {
        return roleRepository.findByEnabled(enabled);
    }
}

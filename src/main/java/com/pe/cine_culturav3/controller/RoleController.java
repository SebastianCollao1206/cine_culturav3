package com.pe.cine_culturav3.controller;

import com.pe.cine_culturav3.model.Role;
import com.pe.cine_culturav3.model.RoleName;
import com.pe.cine_culturav3.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Role>> listarTodos() {
        List<Role> roles = roleService.listarTodos();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Role> buscarPorId(@PathVariable Long id) {
        Optional<Role> role = roleService.buscarPorId(id);
        return role.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Role> buscarPorNombre(@PathVariable RoleName name) {
        Optional<Role> role = roleService.buscarPorNombre(name);
        return role.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> crear(@Validated @RequestBody Role role) {
        try {
            Role nuevoRole = roleService.crear(role);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoRole);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Validated @RequestBody Role role) {
        try {
            role.setId(id);
            Role roleActualizado = roleService.actualizar(role);
            return ResponseEntity.ok(roleActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam Boolean enabled) {
        try {
            Role roleActualizado = roleService.cambiarEstado(id, enabled);
            return ResponseEntity.ok(roleActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/estado/{enabled}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Role>> listarPorEstado(@PathVariable Boolean enabled) {
        List<Role> roles = roleService.listarPorEstado(enabled);
        return ResponseEntity.ok(roles);
    }
}

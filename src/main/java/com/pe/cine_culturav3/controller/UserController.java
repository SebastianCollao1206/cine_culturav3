package com.pe.cine_culturav3.controller;

import com.pe.cine_culturav3.dto.CreateUserDTO;
import com.pe.cine_culturav3.dto.UserResponseDTO;
import com.pe.cine_culturav3.model.User;
import com.pe.cine_culturav3.service.AuthService;
import com.pe.cine_culturav3.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> listarTodos() {
        List<User> users = userService.listarTodos();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> buscarPorId(@PathVariable Long id) {
        Optional<User> user = userService.buscarPorId(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> buscarPorUsername(@PathVariable String username) {
        Optional<User> user = userService.buscarPorUsername(username);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> buscarPorEmail(@PathVariable String email) {
        Optional<User> user = userService.buscarPorEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> crear(@Validated @RequestBody User user) {
        try {
            User nuevoUser = userService.crear(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Validated @RequestBody User user) {
        try {
            user.setId(id);
            User userActualizado = userService.actualizar(user);
            return ResponseEntity.ok(userActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> actualizarParcial(
            @PathVariable Long id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) Boolean enabled) {
        try {
            User userActualizado = userService.actualizarParcial(id, username, email, password, enabled);
            return ResponseEntity.ok(userActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam Boolean enabled) {
        try {
            User userActualizado = userService.cambiarEstado(id, enabled);
            return ResponseEntity.ok(userActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/estado/{enabled}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> listarPorEstado(@PathVariable Boolean enabled) {
        List<User> users = userService.listarPorEstado(enabled);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> asignarRol(@PathVariable Long userId, @PathVariable Long roleId) {
        try {
            User userActualizado = userService.asignarRol(userId, roleId);
            return ResponseEntity.ok(userActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint para que admin cree usuarios con roles específicos
    @PostMapping("/create-user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createUserByAdmin(@Valid @RequestBody CreateUserDTO createUserRequest) {
        try {
            UserResponseDTO response = authService.createUserByAdmin(createUserRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

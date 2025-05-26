package com.pe.cine_culturav3.service;

import com.pe.cine_culturav3.dto.*;
import com.pe.cine_culturav3.model.Role;
import com.pe.cine_culturav3.model.RoleName;
import com.pe.cine_culturav3.model.User;
import com.pe.cine_culturav3.repository.RoleRepository;
import com.pe.cine_culturav3.repository.UserRepository;
import com.pe.cine_culturav3.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    @Transactional
    public AuthResponseDTO login(LoginRequestDTO loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        String jwtToken = jwtService.generateToken(userDetails);

        List<String> roles = user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        return new AuthResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles,
                user.getFechaCreacion(),
                jwtToken,
                "Inicio de sesión exitoso"
        );
    }

    @Transactional
    public AuthResponseDTO registerClient(RegisterClientDTO registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new IllegalArgumentException("El username ya está en uso");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        Role clientRole = roleRepository.findByName(RoleName.ROLE_CLIENT)
                .orElseThrow(() -> new IllegalStateException("Rol CLIENTE no encontrado"));

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setEmail(registerRequest.getEmail());
        newUser.setEnabled(true);
        newUser.setFechaCreacion(LocalDateTime.now());

        newUser.addRole(clientRole);

        User savedUser = userRepository.save(newUser);

        UserDetails userDetails = new CustomUserPrincipal(savedUser);
        String jwtToken = jwtService.generateToken(userDetails);

        List<String> roles = savedUser.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        return new AuthResponseDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                roles,
                savedUser.getFechaCreacion(),
                jwtToken,
                "Registro exitoso"
        );
    }

    @Transactional
    public UserResponseDTO createUserByAdmin(CreateUserDTO createUserRequest) {
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            throw new IllegalArgumentException("El username ya está en uso");
        }

        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new IllegalArgumentException("El email ya está en uso");
        }

        List<Role> roles = new ArrayList<>();
        for (Long roleId : createUserRequest.getRoleIds()) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new IllegalArgumentException("Rol con ID " + roleId + " no encontrado"));
            roles.add(role);
        }

        User newUser = new User();
        newUser.setUsername(createUserRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
        newUser.setEmail(createUserRequest.getEmail());
        newUser.setEnabled(true);
        newUser.setFechaCreacion(LocalDateTime.now());
        newUser.setRoles(roles);

        User savedUser = userRepository.save(newUser);

        List<String> roleNames = savedUser.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toList());

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getEnabled(),
                roleNames,
                savedUser.getFechaCreacion()
        );
    }

    public AuthResponseDTO logout() {
        SecurityContextHolder.clearContext();

        return new AuthResponseDTO(null, null, null, null, null, null,
                "Sesión cerrada exitosamente");
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserPrincipal) {
            CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
            return userPrincipal.getUser();
        }
        throw new IllegalArgumentException("Usuario no autenticado");
    }

    public User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}

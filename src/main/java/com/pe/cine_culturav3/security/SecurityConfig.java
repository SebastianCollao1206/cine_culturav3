package com.pe.cine_culturav3.security;

import com.pe.cine_culturav3.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        prePostEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/roles/**").hasRole("ADMIN")

                        // Endpoints de lectura de categorías - libres
                        .requestMatchers("/api/categorias").permitAll()
                        .requestMatchers("/api/categorias/{id}").permitAll()
                        .requestMatchers("/api/categorias/nombre/{nombre}").permitAll()
                        .requestMatchers("/api/categorias/estado/{enabled}").permitAll()
                        .requestMatchers("/api/categorias/activas").permitAll()

                        // Endpoints de lectura de películas - libres
                        .requestMatchers("/api/peliculas").permitAll()
                        .requestMatchers("/api/peliculas/{id}").permitAll()
                        .requestMatchers("/api/peliculas/categoria/{categoriaId}").permitAll()
                        .requestMatchers("/api/peliculas/usuario/{userId}").permitAll()
                        .requestMatchers("/api/peliculas/buscar").permitAll()
                        .requestMatchers("/api/peliculas/anio/{anio}").permitAll()
                        .requestMatchers("/api/peliculas/ordenadas").permitAll()
                        .requestMatchers("/api/peliculas/categorias-activas").permitAll()
                        .requestMatchers("/api/peliculas/usuarios-activos").permitAll()

                        // Endpoints de métodos de pago - públicos (solo lectura)
                        .requestMatchers("/api/metodos-pago").permitAll()
                        .requestMatchers("/api/metodos-pago/{id}").permitAll()
                        .requestMatchers("/api/metodos-pago/nombre/{nombre}").permitAll()

                        // Endpoints de tipos de suscripción - públicos (solo lectura)
                        .requestMatchers("/api/tipos-suscripcion").permitAll()
                        .requestMatchers("/api/tipos-suscripcion/{id}").permitAll()

                        .anyRequest().authenticated()

                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

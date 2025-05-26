# Cine Cultura v3 - API REST

## 📝 Descripción del Proyecto
Cine Cultura v3 es una API REST desarrollada con Spring Boot que gestiona una plataforma de contenido de peliculas. El sistema permite la administración de películas, categorías, usuarios y sistema de suscripciones.

## 🔐 Características de Seguridad

### Autenticación JWT
- Implementación robusta de JSON Web Tokens (JWT)
- Gestión segura de tokens con:
  - Tiempo de expiración configurable
  - Firma segura con clave secreta
  - Validación de tokens en cada petición
  - Extracción segura de claims y username

### Control de Acceso
- Seguridad basada en roles (ROLE_ADMIN, ROLE_USER, ROLE_CLIENT)
- Endpoints públicos y protegidos claramente definidos
- Preautorización a nivel de método usando @PreAuthorize
- CSRF deshabilitado para arquitectura stateless
- Sesiones stateless con SessionCreationPolicy.STATELESS

## 🔑 Control de Acceso

### Endpoints Públicos
- Autenticación (/api/auth/**)
- Lectura de películas
- Lectura de categorías
- Consulta de métodos de pago
- Información de tipos de suscripción

### Endpoints Protegidos
- Gestión de usuarios (ADMIN)
- Gestión de roles (ADMIN)
- Creación/actualización de películas (ADMIN/USER)
- Gestión de categorías (ADMIN)

## 🏗️ Arquitectura

### Capas del Sistema
- Controllers: Manejo de endpoints REST
- Services: Lógica de negocio
- Repositories: Acceso a datos
- DTOs: Objetos de transferencia de datos
- Models: Entidades de dominio
- Security: seguridad y configuracion

### Seguridad
- SecurityConfig: Configuración central de seguridad
- JwtService: Gestión de tokens JWT
- CustomUserDetailsService: Servicio personalizado de usuarios

## 🛡️ Validaciones
- Validación de entrada con Jakarta Validation
- Manejo de errores personalizado
- Respuestas HTTP apropiadas para cada situación


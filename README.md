# Practica2_TAW-251
Se empieza copiando el archivo de [este repositorio](https://github.com/LiaRos-ai/RegistroUniversitario/tree/7_SEGURIDAD_12052025), para luego agregar y modificar archivos para la realización de la práctica 2.
### Se crearon los siguientes archivos para la implementación del CRUD, excepto Materia que no se creó sino que se modifico los archivos relacionados a este:

## Archivos relacionados con Inscripción
- controller/
  - InscripcionController.java
- model/
  - Inscripcion.java
- dto/
  - InscripcionDTO.java
- repository/
  - InscripcionRepository.java
- service/
  - IInscripcionService.java
  - impl/InscripcionServiceImpl.java
- validator/
  - InscripcionValidator.java

---
## Archivos relacionados con Asignación Docente
- controller/
  - AsignacionDocenteController.java
- model/
  - AsignacionDocente.java
- dto/
  - AsignacionDocenteDTO.java
- repository/
  - AsignacionDocenteRepository.java
- service/
  - IAsignacionDocenteService.java
  - impl/AsignacionDocenteServiceImpl.java
- validator/
  - AsignacionDocenteValidator.java

---
## Archivos relacionados con Materia

- controller/
  - MateriaController.java
- model/
  - Materia.java
- dto/
  - MateriaDTO.java
- repository/
  - MateriaRepository.java
- service/
  - IMateriaService.java
  - impl/MateriaServiceImpl.java
- validator/
  - MateriaValidator.java

---
### Se agregan las validaciones en los archivos:

## Relacionados con AsignacionDocente
- controller/
  - AsignacionDocenteController.java
- model/
  - AsignacionDocente.java
- dto/
  - AsignacionDocenteDTO.java
- validation/
  - AsignacionDocenteValidator.java
---

## Relacionados con Inscripcion
- controller/
  - InscripcionController.java
- model/
  - Inscripcion.java
- dto/
  - InscripcionDTO.java
- validation/
  - InscripcionValidator.java
---

## Relacionados con Materia
- controller/
  - MateriaController.java
- model/
  - Materia.java
- dto/
  - MateriaDTO.java
- validation/
  - MateriaValidator.java   

---
### Se implementa la documentación con swagger en los archivos:
## Relacionados con AsignacionDocente
- controller/
  - AsignacionDocenteController.java
- dto/
  - AsignacionDocenteDTO.java
---

## Relacionados con Inscripcion
- controller/
  - InscripcionController.java
- dto/
  - InscripcionDTO.java
---

## Relacionados con Materia
- controller/
  - MateriaController.java
- dto/
  - MateriaDTO.java
---
### Se protegen los endpoints con JWT
En el archivo se modifica tal que:
- registro/config/SecurityConfig.java
```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(cors -> cors.disable())
        .csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**", "/api/public/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .requestMatchers("/api/admin/**", "/api/asignaciones-docentes/**", "/api/materias/**").hasRole("ADMIN")
            .requestMatchers("/api/docentes/**").hasAnyRole("ADMIN", "DOCENTE")
            .requestMatchers("/api/estudiantes/**", "/api/inscripciones/**").hasAnyRole("ADMIN", "DOCENTE", "ESTUDIANTE")
            .anyRequest().authenticated()
        );
    
    http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
```
---
### En application.properties se realiza la conexión a PostgreSQL
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/Universidad
spring.datasource.username=postgres
spring.datasource.password=123456
spring.datasource.driver-class-name=org.postgresql.Driver
```
---
### Se aplica cache con redis en los archivos:
- impl/
  - AsignacionDocenteServiceImpl.java  
  - InscripcionServiceImpl.java
  - MateriaServiceImpl.java

package com.huerto_hogar.backend.repository;

import com.huerto_hogar.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    // Para buscar un usuario por su UID
    Optional<Usuario> findByUid(String uid);

    // Buscar por email
    Optional<Usuario> findByEmail(String email);

    // Para verificar si un usuario existe por su email
    boolean existsByEmail(String email);

    // Para verificar si un usuario existe por su UID
    boolean existsByUid(String uid);

    // Para eliminar un usuario por su UID
    void deleteByUid(String uid);

    // Para contar el número de usuarios registrados
    long count();

    // Para obtener todos los usuarios
    List<Usuario> findAll();
}

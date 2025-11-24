package com.huerto_hogar.backend.controller;

import com.huerto_hogar.backend.model.Usuario;
import com.huerto_hogar.backend.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Guardar Usuario Nuevo (Registro inicial desde Firebase)
    // POST /api/usuarios
    @PostMapping
    public ResponseEntity<Usuario> guardarUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioService.guardarUsuario(usuario));
    }

    // Obtener Perfil por UID (Para ver roles o datos)
    // GET /api/usuarios/{uid}
    @GetMapping("/{uid}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable String uid) {
        Usuario u = usuarioService.obtenerPorUid(uid);
        if (u == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(u);
    }

    // Listar todos los usuarios (CRUD ADMIN?)
    // GET /api/usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    // Actualizar datos del usuario (Dirección, Teléfono)
    // PUT /api/usuarios/{uid}
    @PutMapping("/{uid}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable String uid, @RequestBody Usuario usuario) {
        // Verificamos que exista antes de actualizar
        Usuario existente = usuarioService.obtenerPorUid(uid);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        // Aseguramos que el UID del objeto coincida con la URL
        usuario.setUid(uid);

        // Mantener el email y rol originales si no vienen en el cuerpo (opcional, por
        // seguridad)
        if (usuario.getRol() == null)
            usuario.setRol(existente.getRol());
        if (usuario.getEmail() == null)
            usuario.setEmail(existente.getEmail());

        usuarioService.actualizarUsuario(usuario);
        return ResponseEntity.ok(usuario);
    }

    // 5. Eliminar usuario
    // DELETE /api/usuarios/{uid}
    @DeleteMapping("/{uid}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable String uid) {
        // Verificamos si existe
        if (usuarioService.obtenerPorUid(uid) == null) {
            return ResponseEntity.notFound().build();
        }

        usuarioService.eliminarPorUid(uid);
        return ResponseEntity.noContent().build();
    }
}
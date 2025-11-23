package com.huerto_hogar.backend.service;

import com.huerto_hogar.backend.model.Usuario;
import com.huerto_hogar.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepo;

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepo.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepo.findAll();
    }

    public Usuario obtenerPorUid(String uid) {
        return usuarioRepo.findByUid(uid).orElse(null);
    }

    public void eliminarPorUid(String uid) {
        usuarioRepo.deleteByUid(uid);
    }

    public void actualizarUsuario(Usuario usuario) {
        usuarioRepo.save(usuario);
    }
}
package com.r4z0r.pridesphere.services;

import com.r4z0r.pridesphere.entity.Usuario;
import com.r4z0r.pridesphere.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Optional<Usuario> findByidPlataforma(String requestId) {
        return usuarioRepository.findByidPlataforma(requestId);
    }

    public boolean existsByIdPlataforma(Long id) {
        return usuarioRepository.existsByIdPlataforma(id);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}

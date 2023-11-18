package com.r4z0r.pridesphere.repositories;

import com.r4z0r.pridesphere.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    @Query("select case when count(u)> 0 then true else false end from Usuario u where u.idPlataforma = ?1")
    boolean existsByIdPlataforma(Long id);

    Usuario findByIdPlataforma(Long id);

    @Query("select u from Usuario u where u.idPlataforma = ?1")
    Optional<Usuario> findByidPlataforma(String requestId);
}

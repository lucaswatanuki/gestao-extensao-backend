package com.ftunicamp.tcc.repositories;

import com.ftunicamp.tcc.model.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    UsuarioEntity findByEmail(String email);

    UsuarioEntity findByCodigoVerificacao(String codigo);
}

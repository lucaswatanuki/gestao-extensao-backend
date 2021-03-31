package com.ftunicamp.tcc.repositories;

import com.ftunicamp.tcc.model.AutorizacaoEntity;
import com.ftunicamp.tcc.model.StatusAutorizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorizacaoRepository extends JpaRepository<AutorizacaoEntity, Long> {

    List<AutorizacaoEntity> findAllByDocente(String docenteUsername);

    List<AutorizacaoEntity> findAllByStatus(StatusAutorizacao status);
}

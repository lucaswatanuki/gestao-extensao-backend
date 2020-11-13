package com.ftunicamp.tcc.repositories;

import com.ftunicamp.tcc.entities.AutorizacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorizacaoRepository extends JpaRepository<AutorizacaoEntity, Long> {

    List<AutorizacaoEntity> findAllByDocente(String docenteUsername);
}

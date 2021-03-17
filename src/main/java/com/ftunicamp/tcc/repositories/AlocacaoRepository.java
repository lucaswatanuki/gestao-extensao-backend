package com.ftunicamp.tcc.repositories;

import com.ftunicamp.tcc.entities.DocenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlocacaoRepository extends JpaRepository<DocenteEntity.Alocacao, Integer> {
}

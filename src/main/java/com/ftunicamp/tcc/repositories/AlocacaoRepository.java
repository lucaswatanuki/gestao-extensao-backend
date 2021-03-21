package com.ftunicamp.tcc.repositories;

import com.ftunicamp.tcc.entities.Alocacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlocacaoRepository extends JpaRepository<Alocacao, Integer> {
}

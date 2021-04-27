package com.ftunicamp.tcc.repositories;

import com.ftunicamp.tcc.model.Alocacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlocacaoRepository extends JpaRepository<Alocacao, Integer> {
    List<Alocacao> findByDocente_id(long docenteId);
    List<Alocacao> findByAtividade_id(long atividadeId);
}

package com.ftunicamp.tcc.repositories;

import com.ftunicamp.tcc.model.Autorizacao;
import com.ftunicamp.tcc.model.StatusAutorizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorizacaoRepository extends JpaRepository<Autorizacao, Long> {

    List<Autorizacao> findAllByDocente(String docenteUsername);

    List<Autorizacao> findAllByStatus(StatusAutorizacao status);

    Optional<Autorizacao> findByAtividade_id(long id);
}

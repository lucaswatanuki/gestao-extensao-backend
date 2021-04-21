package com.ftunicamp.tcc.repositories;

import com.ftunicamp.tcc.model.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArquivosRepository extends JpaRepository<Arquivo, String> {

    List<Arquivo> findAllByAtividadeId(long atividadeId);

    Optional<Arquivo> findByAtividadeId(long atividadeId);
}

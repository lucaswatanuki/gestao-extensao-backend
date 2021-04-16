package com.ftunicamp.tcc.repositories;

import com.ftunicamp.tcc.model.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArquivosRepository extends JpaRepository<Arquivo, String> {

    List<Arquivo> findAllByAtividadeId(long atividadeId);
}

package com.ftunicamp.tcc.repositories;

import com.ftunicamp.tcc.entities.Atividade;
import com.ftunicamp.tcc.entities.DocenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {

    List<Atividade> findAllByDocente(DocenteEntity docente);
}

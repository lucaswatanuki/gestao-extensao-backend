package com.ftunicamp.tcc.repositories;

import com.ftunicamp.tcc.entities.AtividadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtividadeRepository extends JpaRepository<AtividadeEntity, Long> {
}

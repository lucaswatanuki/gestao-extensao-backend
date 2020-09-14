package com.ftunicamp.tcc.repositories;

import com.ftunicamp.tcc.entities.RelatorioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelatorioRepository extends JpaRepository<RelatorioEntity, Long> {
}

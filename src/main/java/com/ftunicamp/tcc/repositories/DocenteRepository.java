package com.ftunicamp.tcc.repositories;

import com.ftunicamp.tcc.entities.DocenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<DocenteEntity, Long> {

    DocenteEntity findByUser_Username(String username);

    Optional<DocenteEntity> findByMatricula(String matricula);

}

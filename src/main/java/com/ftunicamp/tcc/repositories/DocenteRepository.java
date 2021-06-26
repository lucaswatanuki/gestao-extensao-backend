package com.ftunicamp.tcc.repositories;

import com.ftunicamp.tcc.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {

    Docente findByUser_Username(String username);

    Docente findByUser_Id(Long userId);

    Docente findByMatricula(String matricula);

}

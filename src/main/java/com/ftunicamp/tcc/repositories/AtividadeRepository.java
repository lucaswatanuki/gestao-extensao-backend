package com.ftunicamp.tcc.repositories;

import com.ftunicamp.tcc.model.Atividade;
import com.ftunicamp.tcc.model.DocenteEntity;
import com.ftunicamp.tcc.model.StatusAtividade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {

    List<Atividade> findAllByDocente(DocenteEntity docente);

    @Query("from Atividade a where a.docente.id = ?1 and a.status = ?2 and a.dataCriacao between ?3 and ?4")
    List<Atividade> gerarRelatorioAtividadesPorStatusEDocente(long id, StatusAtividade statusAtividade,
                                                              LocalDate dataInicio, LocalDate dataFim);

    @Query("from Atividade a where a.docente.id = ?1 and a.dataCriacao between ?2 and ?3")
    List<Atividade> gerarRelatorioTodasAtividadesPorDocente(long id, LocalDate dataInicio, LocalDate dataFim);

    @Query("from Atividade a where a.dataCriacao between ?1 and ?2")
    List<Atividade> gerarRelatorioTodasAtividades(LocalDate dataInicio, LocalDate dataFim);

    @Query("from Atividade a where a.dataCriacao between ?1 and ?2 and a.status = ?3")
    List<Atividade> gerarRelatorioTodasAtividadesPorStatus(LocalDate dataInicio, LocalDate dataFim, StatusAtividade statusAtividade);
}

package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.response.DashboardResponse;
import com.ftunicamp.tcc.model.Atividade;
import com.ftunicamp.tcc.model.Autorizacao;
import com.ftunicamp.tcc.model.Docente;
import com.ftunicamp.tcc.model.StatusAutorizacao;
import com.ftunicamp.tcc.repositories.AtividadeRepository;
import com.ftunicamp.tcc.repositories.AutorizacaoRepository;
import com.ftunicamp.tcc.repositories.DocenteRepository;
import com.ftunicamp.tcc.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final AtividadeRepository<Atividade> atividadeRepository;
    private final AutorizacaoRepository autorizacaoRepository;
    private final DocenteRepository docenteRepository;

    @Autowired
    public DashboardServiceImpl(AtividadeRepository<Atividade> atividadeRepository, AutorizacaoRepository autorizacaoRepository, DocenteRepository docenteRepository) {
        this.atividadeRepository = atividadeRepository;
        this.autorizacaoRepository = autorizacaoRepository;
        this.docenteRepository = docenteRepository;
    }


    @Override
    public DashboardResponse popularDashboard() {
        var totalAtividades = atividadeRepository.findAll();
        var totalDocentes = docenteRepository.findAll();
        var autorizacoesPendentes = autorizacaoRepository.findAllByStatus(StatusAutorizacao.PENDENTE);

        return mapToDashboardResponse(totalAtividades, totalDocentes, autorizacoesPendentes);
    }

    private DashboardResponse mapToDashboardResponse(java.util.List<com.ftunicamp.tcc.model.Atividade> totalAtividades, java.util.List<Docente> totalDocentes, java.util.List<Autorizacao> autorizacoesPendentes) {
        return DashboardResponse.builder()
                .totalAtividades(totalAtividades.size())
                .autorizacoesPendentes(autorizacoesPendentes.size())
                .totalDocentes(totalDocentes.size())
                .build();
    }
}

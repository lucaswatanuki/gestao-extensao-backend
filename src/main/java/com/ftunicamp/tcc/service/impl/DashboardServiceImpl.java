package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.response.DashboardResponse;
import com.ftunicamp.tcc.entities.StatusAutorizacao;
import com.ftunicamp.tcc.repositories.AtividadeRepository;
import com.ftunicamp.tcc.repositories.AutorizacaoRepository;
import com.ftunicamp.tcc.repositories.DocenteRepository;
import com.ftunicamp.tcc.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final AtividadeRepository atividadeRepository;
    private final AutorizacaoRepository autorizacaoRepository;
    private final DocenteRepository docenteRepository;

    @Autowired
    public DashboardServiceImpl(AtividadeRepository atividadeRepository, AutorizacaoRepository autorizacaoRepository, DocenteRepository docenteRepository) {
        this.atividadeRepository = atividadeRepository;
        this.autorizacaoRepository = autorizacaoRepository;
        this.docenteRepository = docenteRepository;
    }


    @Override
    public DashboardResponse popularDashboard() {
        var response = new DashboardResponse();

        var totalAtividades = atividadeRepository.findAll();
        var totalDocentes = docenteRepository.findAll();
        var autorizacoesPendentes = autorizacaoRepository.findAllByStatus(StatusAutorizacao.PENDENTE);

        response.setAutorizacoesPendentes(autorizacoesPendentes.size());
        response.setTotalAtividades(totalAtividades.size());
        response.setTotalDocentes(totalDocentes.size());

        return response;
    }
}

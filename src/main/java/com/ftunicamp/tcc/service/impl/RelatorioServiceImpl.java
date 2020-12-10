package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.response.RelatorioResponse;
import com.ftunicamp.tcc.repositories.AtividadeRepository;
import com.ftunicamp.tcc.repositories.DocenteRepository;
import com.ftunicamp.tcc.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelatorioServiceImpl implements RelatorioService {

    private final AtividadeRepository atividadeRepository;
    private final DocenteRepository docenteRepository;

    @Autowired
    public RelatorioServiceImpl(AtividadeRepository atividadeRepository,
                                DocenteRepository docenteRepository) {
        this.atividadeRepository = atividadeRepository;
        this.docenteRepository = docenteRepository;
    }

    @Override
    public RelatorioResponse gerarRelatorio(Long idDocente) {
        return null;
    }

    @Override
    public void excluirRelatorio(Long id) {

    }
}

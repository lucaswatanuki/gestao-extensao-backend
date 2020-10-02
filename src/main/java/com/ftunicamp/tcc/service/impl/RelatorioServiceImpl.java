package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.response.RelatorioResponse;
import com.ftunicamp.tcc.repositories.AtividadeRepository;
import com.ftunicamp.tcc.repositories.DocenteRepository;
import com.ftunicamp.tcc.repositories.RelatorioRepository;
import com.ftunicamp.tcc.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelatorioServiceImpl implements RelatorioService {

    private final RelatorioRepository relatorioRepository;
    private final AtividadeRepository atividadeRepository;
    private final DocenteRepository docenteRepository;

    @Autowired
    public RelatorioServiceImpl(RelatorioRepository relatorioRepository,
                                AtividadeRepository atividadeRepository,
                                DocenteRepository docenteRepository) {
        this.relatorioRepository = relatorioRepository;
        this.atividadeRepository = atividadeRepository;
        this.docenteRepository = docenteRepository;
    }

    @Override
    public RelatorioResponse buscarRelatorio(Long id) {
        return null;
    }

    @Override
    public RelatorioResponse gerarRelatorio(Long idAtividade, Long idDocente) {
        return null;
    }

    @Override
    public void excluirRelatorio(Long id) {

    }
}

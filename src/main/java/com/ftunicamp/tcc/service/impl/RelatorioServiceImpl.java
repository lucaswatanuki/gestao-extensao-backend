package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.response.RelatorioResponse;
import com.ftunicamp.tcc.entities.StatusAtividade;
import com.ftunicamp.tcc.repositories.AtividadeRepository;
import com.ftunicamp.tcc.repositories.DocenteRepository;
import com.ftunicamp.tcc.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    public List<RelatorioResponse> gerarRelatorioPorDocente(Long idDocente, StatusAtividade statusAtividade,
                                                            String dataInicio, String dataFim) throws ParseException {

        var inicio = LocalDate.parse(dataInicio);
        var fim = LocalDate.parse(dataFim);

        var atividades = atividadeRepository.gerarRelatorioAtividadesPorStatusEDocente(idDocente, statusAtividade, inicio, fim);

        if (statusAtividade.equals(StatusAtividade.TODOS)) {
            atividades = atividadeRepository.gerarRelatorioTodasAtividadesPorDocente(idDocente, inicio, fim);
        }

        List<RelatorioResponse> response = new ArrayList<>();

        atividades.forEach(atividade -> {
            var responseItem = new RelatorioResponse();
            responseItem.setNomeDocente(atividade.getDocente().getNome());
            responseItem.setStatusAtividade(atividade.getStatus());
            responseItem.setTipoAtividade(atividade.getTipoAtividade());
            responseItem.setDataInicio(atividade.getDataInicio());
            responseItem.setDataFim(atividade.getDataFim());
            response.add(responseItem);
        });

        return response;
    }

    @Override
    public List<RelatorioResponse> gerarRelatorioGeral(StatusAtividade statusAtividade, LocalDateTime dataInicio, LocalDateTime dataFim) {

        return null;
    }
}

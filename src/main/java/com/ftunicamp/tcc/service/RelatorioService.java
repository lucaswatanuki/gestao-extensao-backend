package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.response.RelatorioResponse;
import com.ftunicamp.tcc.entities.StatusAtividade;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;

public interface RelatorioService {

    List<RelatorioResponse> gerarRelatorioPorDocente(Long idDocente, StatusAtividade statusAtividade,
                                                     String dataInicio, String dataFim) throws ParseException;

    List<RelatorioResponse> gerarRelatorioGeral(StatusAtividade statusAtividade, LocalDateTime dataInicio, LocalDateTime dataFim);

}

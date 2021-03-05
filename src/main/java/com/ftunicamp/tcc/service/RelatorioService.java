package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.request.RelatorioRequest;
import com.ftunicamp.tcc.controllers.response.RelatorioResponse;

import java.text.ParseException;
import java.util.List;

public interface RelatorioService {

    List<RelatorioResponse> gerarRelatorioPorDocente(RelatorioRequest request) throws ParseException;

    List<RelatorioResponse> gerarRelatorioGeral(RelatorioRequest request);

}

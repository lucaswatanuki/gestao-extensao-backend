package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.response.RelatorioResponse;

public interface RelatorioService {

    RelatorioResponse gerarRelatorio(Long idDocente);

    void excluirRelatorio(Long id);

}

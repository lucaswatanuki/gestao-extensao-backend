package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.response.RelatorioResponse;

public interface RelatorioService {

    RelatorioResponse buscarRelatorio(Long id);

    RelatorioResponse gerarRelatorio(Long idAtividade, Long idDocente);

    void excluirRelatorio(Long id);

}

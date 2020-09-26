package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.request.AtividadeRequest;
import com.ftunicamp.tcc.controllers.response.AtividadeResponse;

public interface AtividadeService {

    AtividadeResponse cadastrarAtividade(AtividadeRequest request);

    AtividadeResponse buscarAtividade(Long id);

    void excluirAtividade(Long id);

    AtividadeResponse editarAtividade(Long id);
}

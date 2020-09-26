package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.response.AutorizacaoResponse;

public interface AutorizacaoService {

    AutorizacaoResponse incluirAutorizacao(Long idAtividade);

    AutorizacaoResponse buscarAutorizacao(Long idAutorizacao);

    AutorizacaoResponse editarAutorizacao(Long idAutorizacao);

    void excluirAutorizacao(Long idAutorizacao);
}

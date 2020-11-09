package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.response.AutorizacaoResponse;

import java.util.List;

public interface AutorizacaoService {

    AutorizacaoResponse incluirAutorizacao(Long idAtividade);

    AutorizacaoResponse buscarAutorizacao(Long idAutorizacao);

    AutorizacaoResponse editarAutorizacao(Long idAutorizacao);

    List<AutorizacaoResponse> listarAutorizacoes();

    void excluirAutorizacao(Long idAutorizacao);
}

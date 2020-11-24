package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.response.AutorizacaoResponse;
import com.ftunicamp.tcc.entities.StatusAutorizacao;

import java.util.List;

public interface AutorizacaoService {

    AutorizacaoResponse incluirAutorizacao(Long idAtividade);

    AutorizacaoResponse buscarAutorizacao(Long idAutorizacao);

    AutorizacaoResponse editarAutorizacao(Long idAutorizacao, StatusAutorizacao status);

    List<AutorizacaoResponse> listarAutorizacoes();

    void excluirAutorizacao(Long idAutorizacao);
}

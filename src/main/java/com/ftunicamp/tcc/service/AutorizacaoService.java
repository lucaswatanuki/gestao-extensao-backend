package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.request.AutorizacaoRequest;
import com.ftunicamp.tcc.controllers.response.AutorizacaoResponse;
import com.ftunicamp.tcc.model.StatusAutorizacao;

import java.util.List;

public interface AutorizacaoService {

    void incluirAutorizacao(Long idAtividade, AutorizacaoRequest request);

    AutorizacaoResponse buscarAutorizacao(Long idAutorizacao);

    AutorizacaoResponse editarAutorizacao(Long idAutorizacao, StatusAutorizacao status);

    List<AutorizacaoResponse> listarAutorizacoes();

    void excluirAutorizacao(Long idAutorizacao);
}

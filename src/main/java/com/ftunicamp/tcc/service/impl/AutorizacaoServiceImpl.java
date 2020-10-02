package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.response.AutorizacaoResponse;
import com.ftunicamp.tcc.repositories.AutorizacaoRepository;
import com.ftunicamp.tcc.service.AutorizacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorizacaoServiceImpl implements AutorizacaoService {

    private final AutorizacaoRepository autorizacaoRepository;

    @Autowired
    public AutorizacaoServiceImpl(AutorizacaoRepository autorizacaoRepository) {
        this.autorizacaoRepository = autorizacaoRepository;
    }

    @Override
    public AutorizacaoResponse incluirAutorizacao(Long idAtividade) {
        return null;
    }

    @Override
    public AutorizacaoResponse buscarAutorizacao(Long idAutorizacao) {
        return null;
    }

    @Override
    public AutorizacaoResponse editarAutorizacao(Long idAutorizacao) {
        return null;
    }

    @Override
    public void excluirAutorizacao(Long idAutorizacao) {

    }
}

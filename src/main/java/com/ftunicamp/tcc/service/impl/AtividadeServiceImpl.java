package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.request.AtividadeRequest;
import com.ftunicamp.tcc.controllers.response.AtividadeResponse;
import com.ftunicamp.tcc.repositories.AtividadeRepository;
import com.ftunicamp.tcc.security.jwt.JwtUtils;
import com.ftunicamp.tcc.service.AtividadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AtividadeServiceImpl implements AtividadeService {

    private final AtividadeRepository atividadeRepository;

    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    public AtividadeServiceImpl(AtividadeRepository atividadeRepository) {
        this.atividadeRepository = atividadeRepository;
    }

    @Override
    public AtividadeResponse cadastrarAtividade(AtividadeRequest request) {
        return null;
    }

    @Override
    public AtividadeResponse buscarAtividade(Long id) {
        var teste = jwtUtils.getSessao();
        return null;
    }

    @Override
    public void excluirAtividade(Long id) {

    }

    @Override
    public AtividadeResponse editarAtividade(Long id) {
        return null;
    }
}

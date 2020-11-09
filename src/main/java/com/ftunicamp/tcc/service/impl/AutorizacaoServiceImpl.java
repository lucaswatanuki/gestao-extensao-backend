package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.response.AutorizacaoResponse;
import com.ftunicamp.tcc.repositories.AutorizacaoRepository;
import com.ftunicamp.tcc.service.AutorizacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public List<AutorizacaoResponse> listarAutorizacoes() {
        List<AutorizacaoResponse> autorizacaoResponse = new ArrayList<>();
        autorizacaoRepository.findAll().forEach(autorizacao -> {
            var response = new AutorizacaoResponse();
            response.setDataCriacao(autorizacao.getData().toString());
            response.setDocente(autorizacao.getAtividade().getDocente().getNome());
            response.setHoras(autorizacao.getAtividade().getHoraMensal() * autorizacao.getAtividade().getPrazo());
            response.setStatus(autorizacao.getStatus().getStatus());
            response.setId(autorizacao.getId());
            response.setUrgente(autorizacao.getAtividade().isUrgente());

            autorizacaoResponse.add(response);
        });
        return autorizacaoResponse;
    }

    @Override
    public void excluirAutorizacao(Long idAutorizacao) {

    }
}

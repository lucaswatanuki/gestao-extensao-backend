package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.response.AutorizacaoResponse;
import com.ftunicamp.tcc.repositories.AutorizacaoRepository;
import com.ftunicamp.tcc.repositories.DocenteRepository;
import com.ftunicamp.tcc.security.jwt.JwtUtils;
import com.ftunicamp.tcc.service.AutorizacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AutorizacaoServiceImpl implements AutorizacaoService {

    private final AutorizacaoRepository autorizacaoRepository;
    private final JwtUtils jwtUtils;
    private final DocenteRepository docenteRepository;

    @Autowired
    public AutorizacaoServiceImpl(AutorizacaoRepository autorizacaoRepository, JwtUtils jwtUtils, DocenteRepository docenteRepository) {
        this.autorizacaoRepository = autorizacaoRepository;
        this.jwtUtils = jwtUtils;
        this.docenteRepository = docenteRepository;
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
        var sessao = jwtUtils.getSessao();
        var profiles = sessao.getProfiles();
        List<AutorizacaoResponse> autorizacaoResponse = new ArrayList<>();

        if (profiles.stream().anyMatch(profile -> profile.equalsIgnoreCase("ROLE_ADMIN"))) {
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
        } else {
            autorizacaoRepository.findAllByDocente(sessao.getUsername()).forEach(autorizacao -> {
                var response = new AutorizacaoResponse();
                response.setDataCriacao(autorizacao.getData().toString());
                response.setDocente(autorizacao.getAtividade().getDocente().getNome());
                response.setHoras(autorizacao.getAtividade().getHoraMensal() * autorizacao.getAtividade().getPrazo());
                response.setStatus(autorizacao.getStatus().getStatus());
                response.setId(autorizacao.getId());
                response.setUrgente(autorizacao.getAtividade().isUrgente());
                autorizacaoResponse.add(response);
            });
        }

        return autorizacaoResponse;
    }

    @Override
    public void excluirAutorizacao(Long idAutorizacao) {

    }
}

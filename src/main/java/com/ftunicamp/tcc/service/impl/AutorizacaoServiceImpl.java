package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.response.AutorizacaoResponse;
import com.ftunicamp.tcc.entities.StatusAtividade;
import com.ftunicamp.tcc.entities.StatusAutorizacao;
import com.ftunicamp.tcc.exceptions.NegocioException;
import com.ftunicamp.tcc.repositories.AtividadeRepository;
import com.ftunicamp.tcc.repositories.AutorizacaoRepository;
import com.ftunicamp.tcc.repositories.DocenteRepository;
import com.ftunicamp.tcc.security.jwt.JwtUtils;
import com.ftunicamp.tcc.service.AutorizacaoService;
import com.ftunicamp.tcc.service.EmailService;
import com.ftunicamp.tcc.utils.TipoEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Service
public class AutorizacaoServiceImpl implements AutorizacaoService {

    private final AutorizacaoRepository autorizacaoRepository;
    private final JwtUtils jwtUtils;
    private final DocenteRepository docenteRepository;
    private final AtividadeRepository atividadeRepository;
    private final EmailService emailService;

    @Autowired
    public AutorizacaoServiceImpl(AutorizacaoRepository autorizacaoRepository,
                                  JwtUtils jwtUtils,
                                  DocenteRepository docenteRepository,
                                  AtividadeRepository atividadeRepository,
                                  JavaMailSender javaMailSender, EmailService emailService) {
        this.autorizacaoRepository = autorizacaoRepository;
        this.jwtUtils = jwtUtils;
        this.docenteRepository = docenteRepository;
        this.atividadeRepository = atividadeRepository;
        this.emailService = emailService;
    }

    @Override
    public AutorizacaoResponse incluirAutorizacao(Long idAtividade) {
        var autorizacao = autorizacaoRepository.findById(idAtividade);

        autorizacao.ifPresentOrElse(autorizacaoEntity -> {
                    autorizacaoEntity.setStatus(StatusAutorizacao.APROVADO);
                    var atividade = autorizacaoEntity.getAtividade();
                    var docente = atividade.getDocente();

                    if (atividade.getStatus().equals(StatusAtividade.EM_ANDAMENTO)) {
                        var horasEmAndamentoAtualizada = docente.getTotalHorasEmAndamento() + (atividade.getPrazo() * atividade.getHoraMensal());
                        docente.setTotalHorasEmAndamento(horasEmAndamentoAtualizada);
                        docenteRepository.save(docente);
                    }

                    if (atividade.getStatus().equals(StatusAtividade.FUTURA)) {
                        var horasFuturasAtualizada = docente.getTotalHorasFuturas() + (atividade.getPrazo() * atividade.getHoraMensal());
                        docente.setTotalHorasFuturas(horasFuturasAtualizada);
                        docenteRepository.save(docente);
                    }

                    CompletableFuture.runAsync(() -> {
                        try {
                            emailService.enviarEmailAtividade(autorizacaoEntity.getAtividade().getDocente(), TipoEmail.STATUS_ATIVIDADE);
                        } catch (MessagingException | UnsupportedEncodingException e) {
                            Logger.getAnonymousLogger().warning(e.getMessage());
                            e.printStackTrace();
                        }
                    });

                },
                () -> {
                    throw new NegocioException("Autorização não encontrada!");
                });

        return null;
    }

    @Override
    public AutorizacaoResponse buscarAutorizacao(Long idAutorizacao) {
        return null;
    }

    @Override
    public AutorizacaoResponse editarAutorizacao(Long idAutorizacao, StatusAutorizacao status) {
        var autorizacao = autorizacaoRepository.findById(idAutorizacao);

        var response = new AutorizacaoResponse();

        autorizacao.ifPresentOrElse(autorizacaoEntity -> {
                    autorizacaoEntity.setStatus(status);
                    autorizacaoRepository.save(autorizacaoEntity);

                    response.setStatus(autorizacaoEntity.getStatus().getStatus());
                    response.setId(autorizacaoEntity.getId());
                    response.setDocente(autorizacaoEntity.getDocente());
                },
                () -> {
                    throw new NegocioException("Autorização não encontrada!");
                });


        return response;
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

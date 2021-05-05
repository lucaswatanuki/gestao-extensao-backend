package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.request.AutorizacaoRequest;
import com.ftunicamp.tcc.controllers.response.AutorizacaoResponse;
import com.ftunicamp.tcc.exceptions.NegocioException;
import com.ftunicamp.tcc.model.Atividade;
import com.ftunicamp.tcc.model.AutorizacaoEntity;
import com.ftunicamp.tcc.model.StatusAtividade;
import com.ftunicamp.tcc.model.StatusAutorizacao;
import com.ftunicamp.tcc.repositories.AlocacaoRepository;
import com.ftunicamp.tcc.repositories.AtividadeRepository;
import com.ftunicamp.tcc.repositories.AutorizacaoRepository;
import com.ftunicamp.tcc.security.jwt.JwtUtils;
import com.ftunicamp.tcc.service.AutorizacaoService;
import com.ftunicamp.tcc.service.EmailService;
import com.ftunicamp.tcc.utils.TipoEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Service
public class AutorizacaoServiceImpl implements AutorizacaoService {

    private final AutorizacaoRepository autorizacaoRepository;
    private final JwtUtils jwtUtils;
    private final EmailService emailService;
    private final AtividadeRepository<Atividade> atividadeRepository;
    private final AlocacaoRepository alocacaoRepository;

    @Autowired
    public AutorizacaoServiceImpl(AutorizacaoRepository autorizacaoRepository,
                                  JwtUtils jwtUtils,
                                  JavaMailSender javaMailSender,
                                  EmailService emailService,
                                  AtividadeRepository<Atividade> atividadeRepository,
                                  AlocacaoRepository alocacaoRepository) {
        this.autorizacaoRepository = autorizacaoRepository;
        this.jwtUtils = jwtUtils;
        this.emailService = emailService;
        this.atividadeRepository = atividadeRepository;
        this.alocacaoRepository = alocacaoRepository;
    }

    @Override
    @Transactional
    public void incluirAutorizacao(Long idAtividade, AutorizacaoRequest request) {
        final var autorizacao = autorizacaoRepository.findById(idAtividade);

        autorizacao.ifPresentOrElse(autorizacaoEntity -> {
                    var atividade = autorizacaoEntity.getAtividade();
                    var alocacoes = alocacaoRepository.findByAtividade_id(atividade.getId());

                    if (!request.isAutorizado()) {
                        if (alocacoes != null) {
                            alocacaoRepository.deleteInBatch(alocacoes);
                        }
                        autorizacaoEntity.setStatus(StatusAutorizacao.REVISAO);
                        autorizacaoRepository.save(autorizacaoEntity);
                        atividade.setStatus(StatusAtividade.EM_REVISAO);
                        atividade.setRevisao(request.getObservacao());
                        atividadeRepository.save(atividade);
                        enviarEmail(atividade, request.getObservacao());
                    } else {
                        autorizacaoEntity.setStatus(StatusAutorizacao.APROVADO);
                        if (atividade.getStatus().equals(StatusAtividade.PENDENTE)) {
                            alocacoes.forEach(alocacao -> {
                                alocacao.setTotalHorasAprovadas(alocacao.getTotalHorasSolicitadas());
                                alocacaoRepository.save(alocacao);
                            });
                            atividade.setStatus(StatusAtividade.EM_ANDAMENTO);
                            atividadeRepository.save(atividade);
                        }

                        autorizacaoRepository.save(autorizacaoEntity);
                        enviarEmail(atividade, request.getObservacao());
                    }
                },
                () -> {
                    throw new NegocioException("Autorização não encontrada!");
                });
    }

    private void enviarEmail(Atividade atividade, String observacao) {
        CompletableFuture.runAsync(() -> {
            try {
                emailService.enviarEmailAtividade(atividade, TipoEmail.STATUS_ATIVIDADE, observacao);
            } catch (MessagingException | UnsupportedEncodingException e) {
                Logger.getAnonymousLogger().warning(e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @Override
    public AutorizacaoResponse buscarAutorizacao(Long idAutorizacao) {
        return null;
    }

    @Override
    public AutorizacaoResponse editarAutorizacao(Long idAutorizacao, StatusAutorizacao status) {
        final var autorizacao = autorizacaoRepository.findById(idAutorizacao);

        final var response = AutorizacaoResponse.builder();

        autorizacao.ifPresentOrElse(autorizacaoEntity -> {
                    autorizacaoEntity.setStatus(status);
                    autorizacaoRepository.save(autorizacaoEntity);

                    response.status(autorizacaoEntity.getStatus().getStatus())
                            .id(autorizacaoEntity.getId())
                            .docente(autorizacaoEntity.getDocente());

                },
                () -> {
                    throw new NegocioException("Autorização não encontrada!");
                });


        return response.build();
    }

    @Override
    public List<AutorizacaoResponse> listarAutorizacoes() {
        final var sessao = jwtUtils.getSessao();
        final var profiles = sessao.getProfiles();
        final List<AutorizacaoResponse> autorizacaoResponse = new ArrayList<>();

        if (profiles.stream().anyMatch(profile -> profile.equalsIgnoreCase("ROLE_ADMIN"))) {
            autorizacaoRepository.findAll().forEach(autorizacao -> mapToAutorizacaoResponse(autorizacaoResponse, autorizacao));
            return autorizacaoResponse;
        } else {
            autorizacaoRepository.findAllByDocente(sessao.getUsername()).forEach(autorizacao -> mapToAutorizacaoResponse(autorizacaoResponse, autorizacao));
        }

        return autorizacaoResponse;
    }

    private void mapToAutorizacaoResponse(List<AutorizacaoResponse> autorizacaoResponse, AutorizacaoEntity autorizacao) {
        var response = AutorizacaoResponse.builder()
                .dataCriacao(autorizacao.getData().toString())
                .docente(autorizacao.getAtividade().getDocente().getNome())
                .horas(autorizacao.getAtividade().getHoraMensal() * autorizacao.getAtividade().getPrazo())
                .status(autorizacao.getStatus().getStatus())
                .id(autorizacao.getId())
                .urgente(autorizacao.getAtividade().isUrgente())
                .tipoAtividade(autorizacao.getAtividade().getTipoAtividade())
                .build();
        autorizacaoResponse.add(response);
    }

    @Override
    public void excluirAutorizacao(Long idAutorizacao) {
        autorizacaoRepository.deleteById(idAutorizacao);
    }
}

package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.request.ConvenioRequest;
import com.ftunicamp.tcc.controllers.request.CursoExtensaoRequest;
import com.ftunicamp.tcc.controllers.request.RegenciaRequest;
import com.ftunicamp.tcc.controllers.request.UnivespRequest;
import com.ftunicamp.tcc.controllers.response.AtividadeDetalheResponse;
import com.ftunicamp.tcc.controllers.response.AtividadeResponse;
import com.ftunicamp.tcc.controllers.response.Response;
import com.ftunicamp.tcc.entities.*;
import com.ftunicamp.tcc.exceptions.NegocioException;
import com.ftunicamp.tcc.repositories.AtividadeRepository;
import com.ftunicamp.tcc.repositories.AutorizacaoRepository;
import com.ftunicamp.tcc.repositories.DocenteRepository;
import com.ftunicamp.tcc.security.jwt.JwtUtils;
import com.ftunicamp.tcc.service.AtividadeService;
import com.ftunicamp.tcc.service.EmailService;
import com.ftunicamp.tcc.utils.AtividadeFactory;
import com.ftunicamp.tcc.utils.TipoEmail;
import com.ftunicamp.tcc.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.ftunicamp.tcc.utils.DateUtils.*;

@Service
public class AtividadeServiceImpl implements AtividadeService {

    private static final String MENSAGEM_SUCESSO = "Atividade cadastrada com sucesso.";
    private static final String MENSAGEM_ERRO = "Erro ao criar atividade";

    private final AtividadeRepository atividadeRepository;
    private final DocenteRepository docenteRepository;
    private final AutorizacaoRepository autorizacaoRepository;
    private final EmailService emailService;

    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    public AtividadeServiceImpl(AtividadeRepository atividadeRepository,
                                DocenteRepository docenteRepository,
                                AutorizacaoRepository autorizacaoRepository,
                                EmailService emailService) {
        this.atividadeRepository = atividadeRepository;
        this.docenteRepository = docenteRepository;
        this.autorizacaoRepository = autorizacaoRepository;
        this.emailService = emailService;
    }


    @Override
    public Response<String> cadastrarConvenio(ConvenioRequest request) throws UnsupportedEncodingException, MessagingException {
        var docente = (docenteRepository.findByUser_Username(jwtUtils.getSessao().getUsername()));
        var atividade = AtividadeFactory.criarConvenio(request, docente);
        setAlocacao(docente, atividade);
        atividadeRepository.save(atividade);
        salvarAutorizacao(atividade);
        enviarEmailConfirmacao(atividade, docente);
        var response = new Response<String>();
        response.setMensagem(MENSAGEM_SUCESSO);
        return response;
    }

    @Override
    public Response<String> cadastrarCursoExtensao(CursoExtensaoRequest request) {
        var docente = (docenteRepository.findByUser_Username(jwtUtils.getSessao().getUsername()));
        var atividade = AtividadeFactory.criarCurso(request, docente);
        setAlocacao(docente, atividade);
        atividadeRepository.save(atividade);
        salvarAutorizacao(atividade);
        enviarEmailConfirmacao(atividade, docente);
        var response = new Response<String>();
        response.setMensagem(MENSAGEM_SUCESSO);
        return response;
    }

    @Override
    public Response<String> cadastrarRegencia(RegenciaRequest request) {
        var docente = (docenteRepository.findByUser_Username(jwtUtils.getSessao().getUsername()));
        var atividade = AtividadeFactory.criarRegencia(request, docente);

        //Mapear request para entidade - mapper struct

        atividadeRepository.save(atividade);

        var response = new Response<String>();
        response.setMensagem(MENSAGEM_SUCESSO);
        return response;
    }

    @Override
    public Response<String> cadastrarAtividadeUnivesp(UnivespRequest request) {
        var docente = (docenteRepository.findByUser_Username(jwtUtils.getSessao().getUsername()));
        var atividade = AtividadeFactory.criarAtividadeUnivesp(request, docente);

        //Mapear request para entidade - mapper struct

        atividadeRepository.save(atividade);

        var response = new Response<String>();
        response.setMensagem(MENSAGEM_SUCESSO);
        return response;
    }

    @Override
    public AtividadeDetalheResponse buscarAtividade(Long id) {
        var response = AtividadeDetalheResponse.builder();

        var atividadeEntity = atividadeRepository.findById(id);

        atividadeEntity.ifPresentOrElse(atividade -> {
            var docente = atividade.getDocente();
            mapToAtividadeDetalheResponse(response, atividade, docente);
        }, () -> {
            throw new NegocioException("Não foi encontrada nenhuma atividade");
        });

        return response.build();
    }

    private void mapToAtividadeDetalheResponse(AtividadeDetalheResponse.AtividadeDetalheResponseBuilder response, Atividade atividade, DocenteEntity docente) {
        response.id(atividade.getId())
                .docente(docente.getNome())
                .projeto(atividade.getProjeto())
                .valorBruto(atividade.getValorBruto())
                .prazo(atividade.getPrazo())
                .horaMensal(atividade.getHoraMensal())
                .horaSemanal(atividade.getHoraSemanal())
                .dataInicio(LocalDate.from(atividade.getDataInicio()).format(Utilities.formatarData()))
                .dataFim(LocalDate.from(atividade.getDataFim()).format(Utilities.formatarData()))
                .horasAprovadas(docente.getAlocacao()
                        .stream()
                        .map(Alocacao::getTotalHorasAprovadas)
                        .reduce(Long::sum)
                        .orElse(0L))
                .horasSolicitadas(docente.getAlocacao()
                        .stream()
                        .map(Alocacao::getTotalHorasSolicitadas)
                        .reduce(Long::sum)
                        .orElse(0L))
                .observacao(atividade.getObservacao());
    }

    @Override
    public void excluirAtividade(Long id) {
        atividadeRepository.findById(id).ifPresentOrElse(atividadeRepository::delete, () -> {
            throw new NegocioException("Atividade não encontrada");
        });
    }

    @Override
    public AtividadeResponse editarAtividade(Long id) {
        return null;
    }

    @Override
    public List<AtividadeResponse> listarAtividades() {
        List<AtividadeResponse> atividadesResponse = new ArrayList<>();
        var docente = docenteRepository.findByUser_Username(jwtUtils.getSessao().getUsername());
        atividadeRepository.findAllByDocente(docente).forEach(atividade -> {
            var response = AtividadeResponse.builder()
                    .id(atividade.getId())
                    .dataCriacao(atividade.getDataCriacao())
                    .prazo(atividade.getPrazo())
                    .projeto(atividade.getProjeto())
                    .build();
            var statusAtividade = AtividadeFactory.verificaStatusAtividade(atividade);
            if (!atividade.getStatus().equals(statusAtividade)) {
                atividade.setStatus(statusAtividade);
                atividadeRepository.save(atividade);
            }
            atividadesResponse.add(response);
        });

        return atividadesResponse;
    }

    private void salvarAutorizacao(Atividade atividadeEntity) {
        var autorizacao = new AutorizacaoEntity();
        autorizacao.setStatus(StatusAutorizacao.PENDENTE);
        autorizacao.setAtividade(atividadeEntity);
        autorizacao.setData(LocalDate.now());
        autorizacao.setDocente(atividadeEntity.getDocente().getUser().getUsername());
        autorizacaoRepository.save(autorizacao);
    }

    private void setAlocacao(DocenteEntity docente, Atividade atividade) {
        if (!atividade.getStatus().equals(StatusAtividade.CONCLUIDA)) {
            final var horasSolicitadas = atividade.getPrazo() * atividade.getHoraMensal();
            var alocacao = (Alocacao.builder()
                    .ano(getAnoAtual())
                    .semestre(getSemestreAtual(getMesAtual()))
                    .totalHorasSolicitadas(horasSolicitadas)
                    .docente(docente)
                    .atividade(atividade)
                    .build());
            atividade.setAlocacao(alocacao);
        }
    }

    private void enviarEmailConfirmacao(Atividade atividade, DocenteEntity docente) {
        CompletableFuture.runAsync(() -> {
            try {
                emailService.enviarEmailAtividade(docente, TipoEmail.NOVA_ATIVIDADE, atividade.getId(), atividade.getObservacao());
            } catch (MessagingException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }
}

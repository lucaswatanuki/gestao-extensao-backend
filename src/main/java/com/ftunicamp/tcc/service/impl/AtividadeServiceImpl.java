package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.request.ConvenioRequest;
import com.ftunicamp.tcc.controllers.request.CursoExtensaoRequest;
import com.ftunicamp.tcc.controllers.request.RegenciaRequest;
import com.ftunicamp.tcc.controllers.request.UnivespRequest;
import com.ftunicamp.tcc.controllers.response.AtividadeDetalheResponse;
import com.ftunicamp.tcc.controllers.response.AtividadeResponse;
import com.ftunicamp.tcc.controllers.response.Response;
import com.ftunicamp.tcc.entities.Atividade;
import com.ftunicamp.tcc.entities.AutorizacaoEntity;
import com.ftunicamp.tcc.entities.StatusAtividade;
import com.ftunicamp.tcc.entities.StatusAutorizacao;
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
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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
        Atividade atividade = AtividadeFactory.criarConvenio(request, docente);
        atividade = atividadeRepository.save(atividade);

        salvarAutorizacao(atividade);

        CompletableFuture.runAsync(() -> {
            try {
                emailService.enviarEmailAtividade(docente, TipoEmail.NOVA_ATIVIDADE);
            } catch (MessagingException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        var response = new Response<String>();
        response.setMensagem(MENSAGEM_SUCESSO);
        return response;
    }


    @Override
    public Response<String> cadastrarCursoExtensao(CursoExtensaoRequest request) {
        var docente = (docenteRepository.findByUser_Username(jwtUtils.getSessao().getUsername()));
        var atividade = AtividadeFactory.criarCurso(request, docente);

        atividadeRepository.save(atividade);

        salvarAutorizacao(atividade);

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
        var response = new AtividadeDetalheResponse();

        var atividadeEntity = atividadeRepository.findById(id);

        atividadeEntity.ifPresentOrElse(atividade -> {
            var docente = atividade.getDocente();

            response.setId(atividade.getId());
            response.setDocente(docente.getNome());
            response.setProjeto(atividade.getProjeto());
            response.setValorBruto(atividade.getValorBruto());
            response.setPrazo(atividade.getPrazo());
            response.setHoraMensal(atividade.getHoraMensal());
            response.setHoraSemanal(atividade.getHoraSemanal());
            response.setDataInicio(LocalDate.from(atividade.getDataInicio()).format(Utilities.formatarData()));
            response.setDataFim(LocalDate.from(atividade.getDataFim()).format(Utilities.formatarData()));
            response.setHorasEmAndamento(docente.getTotalHorasEmAndamento());
            response.setHorasFuturas(docente.getTotalHorasFuturas());
        }, () -> {
            throw new NegocioException("Não foi encontrada nenhuma atividade");
        });

        return response;
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
            var response = new AtividadeResponse();
            response.setId(atividade.getId());
            response.setDataCriacao(atividade.getDataCriacao());
            response.setPrazo(atividade.getPrazo());
            response.setProjeto(atividade.getProjeto());
            var statusAtividade = AtividadeFactory.verificaStatusAtividade(atividade);
            if (!atividade.getStatus().equals(statusAtividade)) {
                atividade.setStatus(statusAtividade);
                atividadeRepository.save(atividade);
            }
            atividadesResponse.add(response);
        });

        return atividadesResponse;
    }

    private void salvarAutorizacao(Atividade atividade) {
        var autorizacao = new AutorizacaoEntity();
        autorizacao.setStatus(StatusAutorizacao.PENDENTE);
        autorizacao.setAtividade(atividade);
        autorizacao.setData(LocalDate.now());
        autorizacao.setDocente(atividade.getDocente().getUser().getUsername());
        autorizacaoRepository.save(autorizacao);
    }
}

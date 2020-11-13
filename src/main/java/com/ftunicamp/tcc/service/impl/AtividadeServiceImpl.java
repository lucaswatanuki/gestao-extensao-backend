package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.request.ConvenioRequest;
import com.ftunicamp.tcc.controllers.request.CursoExtensaoRequest;
import com.ftunicamp.tcc.controllers.request.RegenciaRequest;
import com.ftunicamp.tcc.controllers.request.UnivespRequest;
import com.ftunicamp.tcc.controllers.response.AtividadeResponse;
import com.ftunicamp.tcc.controllers.response.Response;
import com.ftunicamp.tcc.entities.Atividade;
import com.ftunicamp.tcc.entities.AutorizacaoEntity;
import com.ftunicamp.tcc.entities.StatusAutorizacao;
import com.ftunicamp.tcc.repositories.AtividadeRepository;
import com.ftunicamp.tcc.repositories.AutorizacaoRepository;
import com.ftunicamp.tcc.repositories.DocenteRepository;
import com.ftunicamp.tcc.security.jwt.JwtUtils;
import com.ftunicamp.tcc.service.AtividadeService;
import com.ftunicamp.tcc.utils.AtividadeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AtividadeServiceImpl implements AtividadeService {

    private final String MENSAGEM_SUCESSO = "Atividade Convêncio cadastrada com sucesso.";
    private final String MENSAGEM_ERRO = "Erro ao criar atividade";

    private final AtividadeRepository atividadeRepository;
    private final DocenteRepository docenteRepository;
    private final AutorizacaoRepository autorizacaoRepository;

    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    public AtividadeServiceImpl(AtividadeRepository atividadeRepository,
                                DocenteRepository docenteRepository,
                                AutorizacaoRepository autorizacaoRepository) {
        this.atividadeRepository = atividadeRepository;
        this.docenteRepository = docenteRepository;
        this.autorizacaoRepository = autorizacaoRepository;
    }

    @Override
    public Response<String> cadastrarConvenio(ConvenioRequest request) {
        var docente = (docenteRepository.findByUser_Username(jwtUtils.getSessao().getUsername()));
        Atividade atividade = AtividadeFactory.criarConvenio(request, docente);
        atividade = atividadeRepository.save(atividade);

        salvarAutorizacao(atividade);

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

package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.request.ConvenioRequest;
import com.ftunicamp.tcc.controllers.request.CursoExtensaoRequest;
import com.ftunicamp.tcc.controllers.request.RegenciaRequest;
import com.ftunicamp.tcc.controllers.request.UnivespRequest;
import com.ftunicamp.tcc.controllers.response.*;
import com.ftunicamp.tcc.dto.AlocacaoDto;
import com.ftunicamp.tcc.exceptions.NegocioException;
import com.ftunicamp.tcc.model.*;
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
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AtividadeServiceImpl implements AtividadeService {

    private static final String MENSAGEM_SUCESSO = "Atividade cadastrada com sucesso.";
    private static final String MENSAGEM_ERRO = "Erro ao criar atividade";

    private final AtividadeRepository<Atividade> atividadeRepository;
    private final AtividadeRepository<ConvenioEntity> convenioRepository;
    private final AtividadeRepository<RegenciaEntity> regenciaRepository;
    private final AtividadeRepository<CursoExtensaoEntity> cursoRepository;
    private final DocenteRepository docenteRepository;
    private final AutorizacaoRepository autorizacaoRepository;
    private final EmailService emailService;

    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    public AtividadeServiceImpl(AtividadeRepository<Atividade> atividadeRepository,
                                AtividadeRepository<ConvenioEntity> convenioRepository,
                                AtividadeRepository<RegenciaEntity> regenciaRepository,
                                AtividadeRepository<CursoExtensaoEntity> cursoRepository,
                                DocenteRepository docenteRepository,
                                AutorizacaoRepository autorizacaoRepository,
                                EmailService emailService) {
        this.atividadeRepository = atividadeRepository;
        this.convenioRepository = convenioRepository;
        this.regenciaRepository = regenciaRepository;
        this.cursoRepository = cursoRepository;
        this.docenteRepository = docenteRepository;
        this.autorizacaoRepository = autorizacaoRepository;
        this.emailService = emailService;
    }


    @Override
    public AtividadeResponse cadastrarConvenio(ConvenioRequest request) throws UnsupportedEncodingException, MessagingException {
        var docente = (docenteRepository.findByUser_Username(jwtUtils.getSessao().getUsername()));
        var atividade = AtividadeFactory.criarConvenio(request, docente);
        setAlocacao(docente, atividade, request.getAlocacoes());
        var atividadeId = atividadeRepository.save(atividade).getId();
        salvarAutorizacao(atividade);
        enviarEmailConfirmacao(atividade);
        return AtividadeResponse.builder()
                .id(atividadeId)
                .build();
    }

    @Override
    public AtividadeResponse cadastrarCursoExtensao(CursoExtensaoRequest request) {
        var docente = (docenteRepository.findByUser_Username(jwtUtils.getSessao().getUsername()));
        var atividade = AtividadeFactory.criarCurso(request, docente);
        setAlocacao(docente, atividade, request.getAlocacoes());
        var atividadeId = atividadeRepository.save(atividade).getId();
        salvarAutorizacao(atividade);
        enviarEmailConfirmacao(atividade);
        var response = new Response<String>();
        response.setMensagem(MENSAGEM_SUCESSO);
        return AtividadeResponse.builder()
                .id(atividadeId)
                .build();
    }

    @Override
    public AtividadeResponse cadastrarRegencia(RegenciaRequest request) {
        var docente = (docenteRepository.findByUser_Username(jwtUtils.getSessao().getUsername()));
        var atividade = AtividadeFactory.criarRegencia(request, docente);
        setAlocacao(docente, atividade, request.getAlocacoes());
        var atividadeId = atividadeRepository.save(atividade).getId();
        salvarAutorizacao(atividade);
        enviarEmailConfirmacao(atividade);
        var response = new Response<String>();
        response.setMensagem(MENSAGEM_SUCESSO);
        return AtividadeResponse.builder()
                .id(atividadeId)
                .build();
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
//        var response = AtividadeDetalheResponse.builder();
//
//        var atividadeEntity = atividadeRepository.findById(id);
//
//        atividadeEntity.ifPresentOrElse(atividade -> {
//            var docente = atividade.getDocente();
//            mapToAtividadeDetalheResponse(response, atividade, docente);
//        }, () -> {
//            throw new NegocioException("Não foi encontrada nenhuma atividade");
//        });
//
//        return response.build();

        return null;
    }

//    private void mapToAtividadeDetalheResponse(AtividadeDetalheResponse.AtividadeDetalheResponseBuilder response, Atividade atividade, DocenteEntity docente) {
//        response.id(atividade.getId())
//                .docente(docente.getNome())
//                .projeto(atividade.getProjeto())
//                .valorBruto(atividade.getValorBruto())
//                .prazo(atividade.getPrazo())
//                .horaMensal(atividade.getHoraMensal())
//                .horaSemanal(atividade.getHoraSemanal())
//                .dataInicio(LocalDate.from(atividade.getDataInicio()).format(Utilities.formatarData()))
//                .dataFim(LocalDate.from(atividade.getDataFim()).format(Utilities.formatarData()))
//                .autorizado(atividade.getStatus().equals(StatusAtividade.EM_ANDAMENTO))
//                .tipoAtividade(atividade.getTipoAtividade())
//                .revisao(atividade.getRevisao() == null ? "Não há itens a revisar" : atividade.getRevisao())
//                .observacao(atividade.getObservacao());
//    }

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

    @Override
    public ConvenioDto consultarConvenio(long id) {
        var convenio = convenioRepository.findById(id).orElse(null);

        if (convenio == null) {
            throw new NoSuchElementException("Atividade não encontrada.");
        }

        return ConvenioDto.builder()
                .id(convenio.getId())
                .docente(convenio.getDocente().getNome())
                .projeto(convenio.getProjeto())
                .valorBruto(convenio.getValorBruto())
                .prazo(convenio.getPrazo())
                .horaMensal(convenio.getHoraMensal())
                .horaSemanal(convenio.getHoraSemanal())
                .dataInicio(LocalDate.from(convenio.getDataInicio()).format(Utilities.formatarData()))
                .dataFim(LocalDate.from(convenio.getDataFim()).format(Utilities.formatarData()))
                .autorizado(convenio.getStatus().equals(StatusAtividade.EM_ANDAMENTO))
                .tipoAtividade(convenio.getTipoAtividade())
                .revisao(convenio.getRevisao() == null ? "Não há itens a revisar" : convenio.getRevisao())
                .observacao(convenio.getObservacao())
                .alocacoes(convenio.getAlocacao().stream().map(alocacao -> AlocacaoDto.builder()
                        .id(alocacao.getId())
                        .ano(alocacao.getAno())
                        .semestre(alocacao.getSemestre())
                        .horasAprovadas(alocacao.getTotalHorasAprovadas())
                        .horasSolicitadas(alocacao.getTotalHorasSolicitadas())
                        .build()).collect(Collectors.toList()))
                .descricao(convenio.getDescricao())
                .tipoAtividadeSimultanea(convenio.getTipoAtividadeSimultanea())
                .instituicao(convenio.getInstituicao())
                .build();
    }

    @Override
    public CursoExtensaoDto consultarCursoExtensao(long id) {
        var curso = cursoRepository.findById(id).orElse(null);

        if (curso == null) {
            throw new NoSuchElementException("Atividade não encontrada.");
        }

        return CursoExtensaoDto.builder()
                .id(curso.getId())
                .docente(curso.getDocente().getNome())
                .projeto(curso.getProjeto())
                .valorBruto(curso.getValorBruto())
                .prazo(curso.getPrazo())
                .horaMensal(curso.getHoraMensal())
                .horaSemanal(curso.getHoraSemanal())
                .dataInicio(LocalDate.from(curso.getDataInicio()).format(Utilities.formatarData()))
                .dataFim(LocalDate.from(curso.getDataFim()).format(Utilities.formatarData()))
                .autorizado(curso.getStatus().equals(StatusAtividade.EM_ANDAMENTO))
                .tipoAtividade(curso.getTipoAtividade())
                .revisao(curso.getRevisao() == null ? "Não há itens a revisar" : curso.getRevisao())
                .observacao(curso.getObservacao())
                .alocacoes(curso.getAlocacao().stream().map(alocacao -> AlocacaoDto.builder()
                        .id(alocacao.getId())
                        .ano(alocacao.getAno())
                        .semestre(alocacao.getSemestre())
                        .horasAprovadas(alocacao.getTotalHorasAprovadas())
                        .horasSolicitadas(alocacao.getTotalHorasSolicitadas())
                        .build()).collect(Collectors.toList()))
                .valorBrutoHoraAula(curso.getValorBrutoHoraAula())
                .cargaHorariaTotal(curso.getCargaHorariaTotalDedicada() + curso.getCargaHorariaTotalMinistrada())
                .disciplinas(curso.getDisciplinaParticipacao())
                .valorBrutoTotalAula(curso.getValorBrutoHoraAula())
                .nomeCurso(curso.getProjeto())
                .instituicaoVinculada(curso.getInstituicaoVinculada())
                .participacao(curso.getParticipacao().toString())
                .build();
    }

    @Override
    public RegenciaDto consultarRegencia(long id) {
        var regencia = regenciaRepository.findById(id).orElse(null);

        if (regencia == null) {
            throw new NoSuchElementException("Atividade não encontrada.");
        }

        return RegenciaDto.builder()
                .id(regencia.getId())
                .docente(regencia.getDocente().getNome())
                .projeto(regencia.getProjeto())
                .valorBruto(regencia.getValorBruto())
                .prazo(regencia.getPrazo())
                .horaMensal(regencia.getHoraMensal())
                .horaSemanal(regencia.getHoraSemanal())
                .dataInicio(LocalDate.from(regencia.getDataInicio()).format(Utilities.formatarData()))
                .dataFim(LocalDate.from(regencia.getDataFim()).format(Utilities.formatarData()))
                .autorizado(regencia.getStatus().equals(StatusAtividade.EM_ANDAMENTO))
                .tipoAtividade(regencia.getTipoAtividade())
                .revisao(regencia.getRevisao() == null ? "Não há itens a revisar" : regencia.getRevisao())
                .observacao(regencia.getObservacao())
                .alocacoes(regencia.getAlocacao().stream().map(alocacao -> AlocacaoDto.builder()
                        .id(alocacao.getId())
                        .ano(alocacao.getAno())
                        .semestre(alocacao.getSemestre())
                        .horasAprovadas(alocacao.getTotalHorasAprovadas())
                        .horasSolicitadas(alocacao.getTotalHorasSolicitadas())
                        .build()).collect(Collectors.toList()))
                .valorBrutoHoraAula(regencia.getValorBrutoHoraAula())
                .cargaHorariaTotalDedicada(regencia.getCargaHorariaTotalDedicada())
                .cargaHoraTotalMinistrada(regencia.getCargaHorariaTotalMinistrada())
                .coordenador(regencia.getCoordenador())
                .diasTrabalhadosOutraInstituicao(regencia.getDiasTrabalhadosOutraInstituicao())
                .diasTrabalhadosUnicamp(regencia.getDiasTrabalhadosUnicamp())
                .disciplinaParticipacao(regencia.getDisciplinaParticipacao())
                .curso(regencia.getCurso())
                .nivel(regencia.getNivel())
                .unicoDocente(regencia.isUnicoDocente())
                .responsavel(regencia.isResponsavel())
                .valorBrutoOutraAtividade(regencia.getValorBrutoOutraAtividade())
                .instituicao(regencia.getInstituicao())
                .valorBruto(regencia.getValorBruto())
                .valorBrutoTotalAula(regencia.getValorBrutoHoraAula())
                .build();
    }

    private void salvarAutorizacao(Atividade atividadeEntity) {
        var autorizacao = new AutorizacaoEntity();
        autorizacao.setStatus(StatusAutorizacao.PENDENTE);
        autorizacao.setAtividade(atividadeEntity);
        autorizacao.setData(LocalDate.now());
        autorizacao.setDocente(atividadeEntity.getDocente().getUser().getUsername());
        autorizacaoRepository.save(autorizacao);
    }

    private void setAlocacao(DocenteEntity docente, Atividade atividade, List<AlocacaoDto> alocacoesDto) {
        if (!atividade.getStatus().equals(StatusAtividade.CONCLUIDA)) {
            var alocacoes = alocacoesDto.stream()
                    .map(dto -> {
                        return Alocacao.builder()
                                .ano(dto.getAno())
                                .semestre(dto.getSemestre())
                                .totalHorasSolicitadas(dto.getHorasSolicitadas())
                                .atividade(atividade)
                                .docente(docente)
                                .build();
                    }).collect(Collectors.toList());
            atividade.setAlocacao(alocacoes);
        }
    }

    private void enviarEmailConfirmacao(Atividade atividade) {
        CompletableFuture.runAsync(() -> {
            try {
                emailService.enviarEmailAtividade(atividade, TipoEmail.NOVA_ATIVIDADE, atividade.getObservacao());
            } catch (MessagingException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
    }
}

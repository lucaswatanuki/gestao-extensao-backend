package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.request.ConvenioRequest;
import com.ftunicamp.tcc.controllers.request.CursoExtensaoRequest;
import com.ftunicamp.tcc.controllers.request.RegenciaRequest;
import com.ftunicamp.tcc.controllers.request.UnivespRequest;
import com.ftunicamp.tcc.controllers.response.*;
import com.ftunicamp.tcc.dto.AlocacaoDto;
import com.ftunicamp.tcc.exceptions.NegocioException;
import com.ftunicamp.tcc.model.*;
import com.ftunicamp.tcc.repositories.*;
import com.ftunicamp.tcc.security.jwt.JwtUtils;
import com.ftunicamp.tcc.service.AtividadeService;
import com.ftunicamp.tcc.service.EmailService;
import com.ftunicamp.tcc.utils.AtividadeFactory;
import com.ftunicamp.tcc.utils.TipoEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private static final String ATIVIDADE_ERRO = "Atividade não econtrada.";


    private final AtividadeRepository<Atividade> atividadeRepository;
    private final AtividadeRepository<ConvenioEntity> convenioRepository;
    private final AtividadeRepository<RegenciaEntity> regenciaRepository;
    private final AtividadeRepository<CursoExtensaoEntity> cursoRepository;
    private final DocenteRepository docenteRepository;
    private final AutorizacaoRepository autorizacaoRepository;
    private final EmailService emailService;
    private final ArquivosRepository arquivosRepository;
    private final AlocacaoRepository alocacaoRepository;

    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    public AtividadeServiceImpl(AtividadeRepository<Atividade> atividadeRepository,
                                AtividadeRepository<ConvenioEntity> convenioRepository,
                                AtividadeRepository<RegenciaEntity> regenciaRepository,
                                AtividadeRepository<CursoExtensaoEntity> cursoRepository,
                                DocenteRepository docenteRepository,
                                AutorizacaoRepository autorizacaoRepository,
                                EmailService emailService, ArquivosRepository arquivosRepository, AlocacaoRepository alocacaoRepository) {
        this.atividadeRepository = atividadeRepository;
        this.convenioRepository = convenioRepository;
        this.regenciaRepository = regenciaRepository;
        this.cursoRepository = cursoRepository;
        this.docenteRepository = docenteRepository;
        this.autorizacaoRepository = autorizacaoRepository;
        this.emailService = emailService;
        this.arquivosRepository = arquivosRepository;
        this.alocacaoRepository = alocacaoRepository;
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
    @Transactional
    public void excluirAtividade(Long id) {
        atividadeRepository.findById(id).ifPresentOrElse(atividade -> {
            alocacaoRepository.findByAtividade_id(atividade.getId()).forEach(alocacaoRepository::delete);
            arquivosRepository.findByAtividadeId(atividade.getId()).ifPresent(arquivosRepository::delete);
            autorizacaoRepository.findByAtividade_id(atividade.getId()).ifPresent(autorizacaoRepository::delete);
            atividadeRepository.delete(atividade);
        }, () -> {
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
                .dataInicio(convenio.getDataInicio())
                .dataFim(convenio.getDataFim())
                .autorizado(convenio.getStatus().equals(StatusAtividade.EM_ANDAMENTO))
                .tipoAtividade(convenio.getTipoAtividade())
                .revisao(convenio.getRevisao() == null ? "Não há itens a revisar" : convenio.getRevisao())
                .observacao(convenio.getObservacao())
                .alocacoes(convenio.getAlocacao().stream().map(alocacao -> {
                    var alocacaoDto = new AlocacaoDto();
                    alocacaoDto.setId(alocacao.getId());
                    alocacaoDto.setAno(alocacao.getAno());
                    alocacaoDto.setSemestre(alocacao.getSemestre());
                    alocacaoDto.setHorasAprovadas(alocacao.getTotalHorasAprovadas());
                    alocacaoDto.setHorasSolicitadas(alocacao.getTotalHorasSolicitadas());
                    return alocacaoDto;
                }).collect(Collectors.toList()))
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
                .coordenador(curso.getCoordenador())
                .projeto(curso.getProjeto())
                .valorBruto(curso.getValorBruto())
                .prazo(curso.getPrazo())
                .horaMensal(curso.getHoraMensal())
                .horaSemanal(curso.getHoraSemanal())
                .dataInicio(curso.getDataInicio())
                .dataFim(curso.getDataFim())
                .autorizado(curso.getStatus().equals(StatusAtividade.EM_ANDAMENTO))
                .tipoAtividade(curso.getTipoAtividade())
                .revisao(curso.getRevisao() == null ? "Não há itens a revisar" : curso.getRevisao())
                .observacao(curso.getObservacao())
                .alocacoes(curso.getAlocacao().stream().map(alocacao -> {
                    var alocacaoDto = new AlocacaoDto();
                    alocacaoDto.setId(alocacao.getId());
                    alocacaoDto.setAno(alocacao.getAno());
                    alocacaoDto.setSemestre(alocacao.getSemestre());
                    alocacaoDto.setHorasAprovadas(alocacao.getTotalHorasAprovadas());
                    alocacaoDto.setHorasSolicitadas(alocacao.getTotalHorasSolicitadas());
                    return alocacaoDto;
                }).collect(Collectors.toList()))
                .valorBrutoHoraAula(curso.getValorBrutoHoraAula())
                .valorBrutoOutraAtividade(curso.getValorBrutoOutraAtividade())
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
                .dataInicio(regencia.getDataInicio())
                .dataFim(regencia.getDataFim())
                .autorizado(regencia.getStatus().equals(StatusAtividade.EM_ANDAMENTO))
                .tipoAtividade(regencia.getTipoAtividade())
                .revisao(regencia.getRevisao() == null ? "Não há itens a revisar" : regencia.getRevisao())
                .observacao(regencia.getObservacao())
                .alocacoes(regencia.getAlocacao().stream().map(alocacao -> {
                    var alocacaoDto = new AlocacaoDto();
                    alocacaoDto.setId(alocacao.getId());
                    alocacaoDto.setAno(alocacao.getAno());
                    alocacaoDto.setSemestre(alocacao.getSemestre());
                    alocacaoDto.setHorasAprovadas(alocacao.getTotalHorasAprovadas());
                    alocacaoDto.setHorasSolicitadas(alocacao.getTotalHorasSolicitadas());
                    return alocacaoDto;
                }).collect(Collectors.toList()))
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

    @Override
    public void updateConvenio(ConvenioDto request) {
        convenioRepository.findById(request.getId())
                .ifPresentOrElse(convenio -> convenioRepository.save(AtividadeFactory.updateConvenio(request, convenio)), () -> {
            throw new NoSuchElementException(ATIVIDADE_ERRO);
        });
    }

    @Override
    public void updateCursoExtensao(CursoExtensaoDto request) {
        cursoRepository.findById(request.getId())
                .ifPresentOrElse(curso -> cursoRepository.save(AtividadeFactory.updateCurso(request, curso)), () -> {
            throw new NoSuchElementException(ATIVIDADE_ERRO);
        });
    }

    @Override
    public void updateRegencia(RegenciaDto request) {
        regenciaRepository.findById(request.getId())
                .ifPresentOrElse(regencia -> regenciaRepository.save(AtividadeFactory.updateRegencia(request, regencia)), () -> {
            throw new NoSuchElementException(ATIVIDADE_ERRO);
        });
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

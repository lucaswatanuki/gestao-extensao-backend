package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.request.UnivespRequest;
import com.ftunicamp.tcc.controllers.response.AtividadeResponse;
import com.ftunicamp.tcc.controllers.response.RegenciaDto;
import com.ftunicamp.tcc.controllers.response.Response;
import com.ftunicamp.tcc.dto.AlocacaoDto;
import com.ftunicamp.tcc.dto.ConvenioDto;
import com.ftunicamp.tcc.dto.CursoExtensaoDto;
import com.ftunicamp.tcc.exceptions.NegocioException;
import com.ftunicamp.tcc.mappers.ConvenioMapper;
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

import static com.ftunicamp.tcc.utils.AtividadeFactory.verificaStatusAtividade;

@Service
public class AtividadeServiceImpl implements AtividadeService {

    private static final String MENSAGEM_SUCESSO = "Atividade cadastrada com sucesso.";
    private static final String ATIVIDADE_NOT_FOUND = "Atividade não econtrada.";
    public static final String ITENS_REVISAO = "Não há itens a revisar";

    private final AtividadeRepository<Atividade> atividadeRepository;
    private final AtividadeRepository<Convenio> convenioRepository;
    private final AtividadeRepository<Regencia> regenciaRepository;
    private final AtividadeRepository<CursoExtensao> cursoRepository;
    private final DocenteRepository docenteRepository;
    private final AutorizacaoRepository autorizacaoRepository;
    private final EmailService emailService;
    private final ArquivosRepository arquivosRepository;
    private final AlocacaoRepository alocacaoRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public AtividadeServiceImpl(AtividadeRepository<Atividade> atividadeRepository,
                                AtividadeRepository<Convenio> convenioRepository,
                                AtividadeRepository<Regencia> regenciaRepository,
                                AtividadeRepository<CursoExtensao> cursoRepository,
                                DocenteRepository docenteRepository,
                                AutorizacaoRepository autorizacaoRepository,
                                EmailService emailService, ArquivosRepository arquivosRepository,
                                AlocacaoRepository alocacaoRepository, JwtUtils jwtUtils) {
        this.atividadeRepository = atividadeRepository;
        this.convenioRepository = convenioRepository;
        this.regenciaRepository = regenciaRepository;
        this.cursoRepository = cursoRepository;
        this.docenteRepository = docenteRepository;
        this.autorizacaoRepository = autorizacaoRepository;
        this.emailService = emailService;
        this.arquivosRepository = arquivosRepository;
        this.alocacaoRepository = alocacaoRepository;
        this.jwtUtils = jwtUtils;
    }


    @Override
    public AtividadeResponse cadastrarConvenio(ConvenioDto request) {
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
    public AtividadeResponse cadastrarCursoExtensao(CursoExtensaoDto request) {
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
    public AtividadeResponse cadastrarRegencia(RegenciaDto request) {
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
            var statusAtividade = verificaStatusAtividade(atividade);
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
            throw new NoSuchElementException(ATIVIDADE_NOT_FOUND);
        }

        var convenioDto = ConvenioMapper.INSTANCE.mapToConvenioDto(convenio);
        convenioDto.setAlocacoes(getAlocacoesSemestral(convenio));

        return convenioDto;
    }

    private <T extends Atividade> boolean verificaLimiteHorasConvenios(T atividade) {
        var horasAprovadas = atividade.getDocente().getAlocacao().stream()
                .filter(alocacoesDocente -> atividade.getAlocacao()
                        .stream()
                        .filter(a -> a.getSemestre() == alocacoesDocente.getSemestre())
                        .filter(a -> a.getAno() == alocacoesDocente.getAno())
                        .findAny()
                        .orElse(null) != null
                )
                .map(Alocacao::getTotalHorasAprovadas)
                .reduce(Long::sum)
                .orElse(0L);

        return horasAprovadas >= 60;
    }

    private <T extends Atividade> List<AlocacaoDto> getAlocacoesSemestral(T atividade) {
        return atividade.getAlocacao()
                .stream()
                .map(alocacao -> {
                    var alocacaoDto = new AlocacaoDto();
                    alocacaoDto.setId(alocacao.getId());
                    alocacaoDto.setAtividadeId(alocacao.getAtividade().getId());
                    alocacaoDto.setAno(alocacao.getAno());
                    alocacaoDto.setSemestre(alocacao.getSemestre());
                    alocacaoDto.setTipoAtividade(alocacao.getAtividade().getTipoAtividade());
                    alocacaoDto.setHorasAprovadas(alocacao.getTotalHorasAprovadas());
                    alocacaoDto.setHorasSolicitadas(alocacao.getTotalHorasSolicitadas());
                    alocacaoDto.setStatus(alocacao.getAtividade().getAutorizacao().getStatus());
                    alocacaoDto.setHorasAprovadasConvenio(alocacao.getDocente().getAlocacao()
                            .stream()
                            .filter(x -> x.getAtividade().getTipoAtividade().contentEquals(TipoAtividade.CONVENIO.toString()))
                            .filter(x -> x.getSemestre() == alocacao.getSemestre())
                            .filter(x -> x.getAno() == alocacao.getAno())
                            .map(Alocacao::getTotalHorasAprovadas)
                            .reduce(Long::sum)
                            .orElse(0L));
                    alocacaoDto.setHorasAprovadasRegencia(alocacao.getAtividade().getAlocacao()
                            .stream()
                            .filter(x -> x.getAtividade().getTipoAtividade().equals(TipoAtividade.REGENCIA.toString()))
                            .filter(x -> x.getSemestre() == alocacao.getSemestre())
                            .filter(x -> x.getAno() == alocacao.getAno())
                            .map(Alocacao::getTotalHorasAprovadas)
                            .reduce(Long::sum)
                            .orElse(0L));
                    alocacaoDto.setHorasAprovadasCurso(alocacao.getAtividade().getAlocacao()
                            .stream()
                            .filter(x -> x.getAtividade().getTipoAtividade().equals(TipoAtividade.CURSO.toString()))
                            .filter(x -> x.getSemestre() == alocacao.getSemestre())
                            .filter(x -> x.getAno() == alocacao.getAno())
                            .map(Alocacao::getTotalHorasAprovadas)
                            .reduce(Long::sum)
                            .orElse(0L));

                    return alocacaoDto;
                }).collect(Collectors.toList());
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
                .dataInicio(curso.getDataInicio())
                .dataFim(curso.getDataFim())
                .autorizado(curso.getStatus().equals(StatusAtividade.EM_ANDAMENTO))
                .tipoAtividade(curso.getTipoAtividade())
                .revisao(curso.getRevisao() == null ? ITENS_REVISAO : curso.getRevisao())
                .observacao(curso.getObservacao())
                .alocacoes(getAlocacoesSemestral(curso))
                .valorBrutoHora(curso.getValorBrutoHora())
                .valorBrutoTotal(curso.getValorBrutoTotal())
                .disciplinas(curso.getDisciplinaParticipacao())
                .totalHorasMinistradas(curso.getTotalHorasMinistradas())
                .totalHorasOutrasAtividades(curso.getTotalHorasOutrasAtividades())
                .nomeCurso(curso.getProjeto())
                .instituicaoVinculada(curso.getInstituicaoVinculada())
                .participacao(curso.getParticipacao().toString())
                .excedido(verificaLimiteHorasConvenios(curso))
                .build();
    }

    @Override
    public RegenciaDto consultarRegencia(long id) {
        var regencia = regenciaRepository.findById(id).orElse(null);

        if (regencia == null) {
            throw new NoSuchElementException(ATIVIDADE_NOT_FOUND);
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
                .revisao(regencia.getRevisao() == null ? ITENS_REVISAO : regencia.getRevisao())
                .observacao(regencia.getObservacao())
                .alocacoes(getAlocacoesSemestral(regencia))
                .valorBrutoHora(regencia.getValorBrutoHora())
                .valorBrutoTotal(regencia.getValorBrutoTotal())
                .totalHorasMinistradas(regencia.getTotalHorasMinistradas())
                .totalHorasOutrasAtividades(regencia.getTotalHorasOutrasAtividades())
                .coordenador(regencia.getCoordenador())
                .diasTrabalhadosOutraInstituicao(regencia.getDiasTrabalhadosOutraInstituicao())
                .diasTrabalhadosUnicamp(regencia.getDiasTrabalhadosUnicamp())
                .disciplinaParticipacao(regencia.getDisciplinaParticipacao())
                .curso(regencia.getCurso())
                .nivel(regencia.getNivel())
                .unicoDocente(regencia.isUnicoDocente())
                .responsavel(regencia.isResponsavel())
                .instituicao(regencia.getInstituicao())
                .valorBruto(regencia.getValorBruto())
                .excedido(verificaLimiteHorasConvenios(regencia))
                .build();
    }

    @Override
    public void updateConvenio(ConvenioDto request) {
        convenioRepository.findById(request.getId())
                .ifPresentOrElse(convenio -> convenioRepository.save(AtividadeFactory.updateConvenio(request, convenio)), () -> {
                    throw new NoSuchElementException(ATIVIDADE_NOT_FOUND);
                });
    }

    @Override
    public void updateCursoExtensao(CursoExtensaoDto request) {
        cursoRepository.findById(request.getId())
                .ifPresentOrElse(curso -> cursoRepository.save(AtividadeFactory.updateCurso(request, curso)), () -> {
                    throw new NoSuchElementException(ATIVIDADE_NOT_FOUND);
                });
    }

    @Override
    public void updateRegencia(RegenciaDto request) {
        regenciaRepository.findById(request.getId())
                .ifPresentOrElse(regencia -> regenciaRepository.save(AtividadeFactory.updateRegencia(request, regencia)), () -> {
                    throw new NoSuchElementException(ATIVIDADE_NOT_FOUND);
                });
    }

    private void salvarAutorizacao(Atividade atividadeEntity) {
        var autorizacao = new Autorizacao();
        autorizacao.setStatus(StatusAutorizacao.PENDENTE);
        autorizacao.setAtividade(atividadeEntity);
        autorizacao.setData(LocalDate.now());
        autorizacao.setDocente(atividadeEntity.getDocente().getUser().getUsername());
        autorizacaoRepository.save(autorizacao);
    }

    private void setAlocacao(Docente docente, Atividade atividade, List<AlocacaoDto> alocacoesDto) {
        if (!atividade.getStatus().equals(StatusAtividade.CONCLUIDA)) {
            var alocacoes = alocacoesDto.stream()
                    .map(dto -> Alocacao.builder()
                            .ano(dto.getAno())
                            .semestre(dto.getSemestre())
                            .totalHorasSolicitadas(dto.getHorasSolicitadas())
                            .atividade(atividade)
                            .docente(docente)
                            .build()
                    ).collect(Collectors.toList());
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

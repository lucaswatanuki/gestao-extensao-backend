package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.response.DocenteResponse;
import com.ftunicamp.tcc.dto.AlocacaoDto;
import com.ftunicamp.tcc.dto.UsuarioDto;
import com.ftunicamp.tcc.exceptions.NegocioException;
import com.ftunicamp.tcc.model.Alocacao;
import com.ftunicamp.tcc.model.DocenteEntity;
import com.ftunicamp.tcc.model.StatusAtividade;
import com.ftunicamp.tcc.repositories.AlocacaoRepository;
import com.ftunicamp.tcc.repositories.DocenteRepository;
import com.ftunicamp.tcc.repositories.UserRepository;
import com.ftunicamp.tcc.security.jwt.JwtUtils;
import com.ftunicamp.tcc.service.DocenteService;
import com.ftunicamp.tcc.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.ftunicamp.tcc.utils.DateUtils.getAnoAtual;

@Service
public class DocenteServiceImpl implements DocenteService, UsuarioService {

    private final DocenteRepository docenteRepository;
    private final UserRepository userRepository;
    private final AlocacaoRepository alocacaoRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public DocenteServiceImpl(DocenteRepository docenteRepository, UserRepository userRepository,
                              AlocacaoRepository alocacaoRepository, JwtUtils jwtUtils) {
        this.docenteRepository = docenteRepository;
        this.userRepository = userRepository;
        this.alocacaoRepository = alocacaoRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public List<DocenteResponse> listarDocentes() {

        List<DocenteResponse> response = new ArrayList<>();

        List<DocenteEntity> docentes = docenteRepository.findAll();

        docentes.forEach(docente -> {
            var docenteResponse = new DocenteResponse();
            docenteResponse.setId(docente.getId());
            docenteResponse.setNome(docente.getNome());
            docenteResponse.setMatricula(docente.getMatricula());
            docenteResponse.setEmail(docente.getEmail());
            docenteResponse.setAutorizado(docente.isAutorizado());
            docenteResponse.setTotalHorasAprovadas(docente.getAlocacao()
                    .stream()
                    .filter(alocacao -> alocacao.getAno() == getAnoAtual())
                    .map(Alocacao::getTotalHorasAprovadas)
                    .reduce(Long::sum).orElse(0L)
            );
            docenteResponse.setTotalHorasSolicitadas(docente.getAlocacao()
                    .stream()
                    .filter(alocacao -> alocacao.getAno() == getAnoAtual())
                    .map(Alocacao::getTotalHorasSolicitadas)
                    .reduce(Long::sum).orElse(0L));
            response.add(docenteResponse);
        });

        return response;
    }

    @Override
    public DocenteResponse consultarDocente(Long id) {
        var docente = docenteRepository.findById(id);

        if (docente.isEmpty()) {
            throw new NegocioException("Docente não cadastrado");
        }

        var atividades = docente.get().getAtividades();

//        atividades.forEach(atividade -> {
//            var totalHorasAtividade = atividade.getHoraMensal() * atividade.getPrazo();
//            var statusAtividadeAtualizado = AtividadeFactory.verificaStatusAtividade(atividade);
//            if (!atividade.getStatus().equals(statusAtividadeAtualizado)) {
//                atividade.setStatus(statusAtividadeAtualizado);
//                atividadeRepository.save(atividade);
//
//                if (statusAtividadeAtualizado.equals(StatusAtividade.CONCLUIDA)) {
//                    var alocacao = docente.get().getAlocacao();
//                    docente.get().set(docente.get().getTotalHorasEmAndamento() - totalHorasAtividade);
//                    docenteRepository.save(docente.get());
//                }
//                if (statusAtividadeAtualizado.equals(StatusAtividade.EM_ANDAMENTO)) {
//                    docente.get().setTotalHorasEmAndamento(docente.get().getTotalHorasEmAndamento() + totalHorasAtividade);
//                    docente.get().setTotalHorasFuturas(docente.get().getTotalHorasFuturas() - totalHorasAtividade);
//                    docenteRepository.save(docente.get());
//                }
//            }
//        });
//
//        var response = new DocenteResponse();
//        response.setTotalHorasEmAndamento(docente.get().getTotalHorasEmAndamento());
//        response.setTotalHorasFuturas(docente.get().getTotalHorasFuturas());

        return null;
    }

    @Override
    public void deletarDocente(String username) {
        var docente = docenteRepository.findByUser_Username(username);
        docenteRepository.delete(docente);
        var user = userRepository.findByUsername(username);
        user.ifPresent(userRepository::delete);
    }

    @Override
    public List<AlocacaoDto> consultarAlocacoesDocente(long docenteId) {
        var alocacoes = alocacaoRepository.findByDocente_id(docenteId);

        return alocacoes.stream()
                .filter(alocacao -> alocacao.getAtividade().getStatus().equals(StatusAtividade.CONCLUIDA) ||
                        alocacao.getAtividade().getStatus().equals(StatusAtividade.EM_ANDAMENTO))
                .map(alocacao -> AlocacaoDto.builder()
                        .id(alocacao.getAtividade().getId())
                        .ano(alocacao.getAno())
                        .semestre(alocacao.getSemestre())
                        .horasAprovadas(alocacao.getTotalHorasAprovadas())
                        .tipoAtividade(alocacao.getAtividade().getTipoAtividade())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<AlocacaoDto> getAlocacoes() {
        final var sessao = jwtUtils.getSessao();
        final var profiles = sessao.getProfiles();

        if (profiles.stream().noneMatch(profile -> profile.equalsIgnoreCase("ROLE_ADMIN"))) {
            var docente = docenteRepository.findByUser_Username(jwtUtils.getSessao().getUsername());
            return alocacaoRepository.findByDocente_id(docente.getId())
                    .stream()
                    .map(this::mapToAlocacaoDto)
                    .collect(Collectors.toList());
        }

        return alocacaoRepository.findAll()
                .stream()
                .map(this::mapToAlocacaoDto)
                .collect(Collectors.toList());
    }

    private AlocacaoDto mapToAlocacaoDto(Alocacao alocacao) {
        return AlocacaoDto.builder()
                .id(alocacao.getId())
                .ano(alocacao.getAno())
                .semestre(alocacao.getSemestre())
                .horasAprovadas(alocacao.getTotalHorasAprovadas())
                .horasSolicitadas(alocacao.getTotalHorasSolicitadas())
                .tipoAtividade(alocacao.getAtividade().getTipoAtividade())
                .build();
    }

    @Override
    public UsuarioDto getDadosUsuario(long id) {
        var docente = docenteRepository.findByUser_Id(id);

        return UsuarioDto.builder()
                .nome(docente.getNome())
                .telefone(docente.getTelefone())
                .email(docente.getEmail())
                .matricula(docente.getMatricula())
                .endereco(docente.getEndereco())
                .build();
    }

    @Override
    public void alterarDadosUsuario(long id, UsuarioDto request) {
        var docente = docenteRepository.findByUser_Id(id);

        if (request.getNome() != null) {
            docente.setNome(request.getNome());
        }

        if (request.getTelefone() != null) {
            docente.setTelefone(request.getTelefone());
        }

        docenteRepository.save(docente);
    }
}

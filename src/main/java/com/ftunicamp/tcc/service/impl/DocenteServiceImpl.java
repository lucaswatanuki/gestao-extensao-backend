package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.response.DocenteResponse;
import com.ftunicamp.tcc.dto.AlocacaoDto;
import com.ftunicamp.tcc.dto.UsuarioDto;
import com.ftunicamp.tcc.exceptions.NegocioException;
import com.ftunicamp.tcc.model.*;
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

        List<Docente> docentes = docenteRepository.findAll();

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
                .map(alocacao -> {
                    var alocacaoDto = new AlocacaoDto();
                    alocacaoDto.setId(alocacao.getId());
                    alocacaoDto.setAno(alocacao.getAno());
                    alocacaoDto.setSemestre(alocacao.getSemestre());
                    alocacaoDto.setHorasAprovadas(alocacao.getTotalHorasAprovadas());
                    alocacaoDto.setHorasSolicitadas(alocacao.getTotalHorasSolicitadas());
                    alocacaoDto.setTipoAtividade(alocacao.getAtividade().getTipoAtividade());
                    alocacaoDto.setStatus(alocacao.getAtividade().getAutorizacao().getStatus());
                    return alocacaoDto;
                })
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

    @Override
    public void atualizarAlocacao(AlocacaoDto dto) {
        alocacaoRepository.findById(dto.getId()).ifPresent(alocacao -> {
            if (alocacao.getAtividade().getAutorizacao().getStatus().equals(StatusAutorizacao.APROVADO)) {
                throw new NegocioException("Não é possível alterar alocação para atividade aprovada.");
            }
            alocacao.setAno(dto.getAno());
            alocacao.setSemestre(dto.getSemestre());
            alocacao.setTotalHorasSolicitadas(dto.getHorasSolicitadas());
            alocacaoRepository.save(alocacao);
        });
    }

    private AlocacaoDto mapToAlocacaoDto(Alocacao alocacao) {
        var alocacaoDto = new AlocacaoDto();
        alocacaoDto.setId(alocacao.getId());
        alocacaoDto.setAtividadeId(alocacao.getAtividade().getId());
        alocacaoDto.setAno(alocacao.getAno());
        alocacaoDto.setSemestre(alocacao.getSemestre());
        alocacaoDto.setHorasAprovadas(alocacao.getTotalHorasAprovadas());
        alocacaoDto.setHorasSolicitadas(alocacao.getTotalHorasSolicitadas());
        alocacaoDto.setTipoAtividade(alocacao.getAtividade().getTipoAtividade());
        alocacaoDto.setStatus(alocacao.getAtividade().getAutorizacao().getStatus());
        return alocacaoDto;
    }

    @Override
    public UsuarioDto getDadosUsuario(long id) {
        var docente = docenteRepository.findByUser_Id(id);

        var response = new UsuarioDto();
        response.setNome(docente.getNome());
        response.setEmail(docente.getEmail());
        response.setEndereco(docente.getEndereco());
        response.setMatricula(docente.getMatricula());
        response.setTitulo(docente.getTitulo().getValue());
        response.setTelefone(docente.getTelefone());

        return response;
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

        if (request.getTitulo() != null) {
            docente.setTitulo(Titulo.fromString(request.getTitulo()));
        }

        docenteRepository.save(docente);
    }
}

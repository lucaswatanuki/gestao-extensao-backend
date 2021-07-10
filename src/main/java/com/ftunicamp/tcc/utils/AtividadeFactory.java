package com.ftunicamp.tcc.utils;

import com.ftunicamp.tcc.controllers.request.Participacao;
import com.ftunicamp.tcc.controllers.request.RegenciaRequest;
import com.ftunicamp.tcc.controllers.request.UnivespRequest;
import com.ftunicamp.tcc.controllers.response.RegenciaDto;
import com.ftunicamp.tcc.dto.ConvenioDto;
import com.ftunicamp.tcc.dto.CursoExtensaoDto;
import com.ftunicamp.tcc.exceptions.NegocioException;
import com.ftunicamp.tcc.mappers.ConvenioMapper;
import com.ftunicamp.tcc.mappers.CursoMapper;
import com.ftunicamp.tcc.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

@Component
public class AtividadeFactory {

    private AtividadeFactory() {
    }

    public static Atividade criarConvenio(ConvenioDto request, Docente docente) {
        if (request.getDataInicio().isAfter(request.getDataFim())) {
            throw new NegocioException("Verificar datas de inicio e fim!");
        }

        var convenio = ConvenioMapper.INSTANCE.mapToConvenio(request, docente, LocalDate.now());
        convenio.setStatus(verificaStatusAtividade(convenio));
        return convenio;
    }

    public static Convenio updateConvenio(ConvenioDto request, Convenio convenio) {
        convenio.setHoraMensal(request.getHoraMensal());
        convenio.setHoraSemanal(request.getHoraSemanal());
        convenio.setValorBruto(request.getValorBruto());
        convenio.setPrazo(ChronoUnit.MONTHS.between(YearMonth.from(request.getDataInicio()), YearMonth.from(request.getDataFim())));
        convenio.setCoordenador(request.getCoordenador());
        convenio.setDescricao(request.getDescricao());
        convenio.setInstituicao(request.getInstituicao());
        convenio.setProjeto(request.getProjeto());
        convenio.setDataInicio(request.getDataInicio());
        convenio.setDataFim(request.getDataFim());
        convenio.setDataModificacao(LocalDate.now());
        convenio.setObservacao(request.getObservacao() == null ? "" : request.getObservacao());
        convenio.setTipoAtividadeSimultanea(request.getTipoAtividadeSimultanea());
        return convenio;
    }

    public static CursoExtensao criarCurso(CursoExtensaoDto request, Docente docente) {
        var curso = CursoMapper.INSTANCE.mapToCursoExtensao(request, docente, LocalDate.now());
        curso.setStatus(verificaStatusAtividade(curso));
        return curso;
    }

    public static CursoExtensao updateCurso(CursoExtensaoDto request, CursoExtensao curso) {
        curso.setCoordenador(request.getCoordenador());
        curso.setProjeto(request.getNomeCurso());
        curso.setDisciplinaParticipacao(request.getDisciplinas());
        curso.setDataInicio(request.getDataInicio());
        curso.setDataFim(request.getDataFim());
        curso.setValorBruto(request.getValorBrutoTotal());
        curso.setValorBrutoTotal(request.getValorBrutoTotal());
        curso.setValorBrutoHora(request.getValorBrutoHora());
        curso.setTotalHorasMinistradas(request.getTotalHorasMinistradas());
        curso.setTotalHorasOutrasAtividades(request.getTotalHorasOutrasAtividades());
        curso.setHoraMensal(request.getHoraMensal());
        curso.setHoraSemanal(request.getHoraSemanal());
        curso.setParticipacao(Participacao.valueOf(request.getParticipacao()));
        curso.setObservacao(request.getObservacao() == null ? "" : request.getObservacao());
        curso.setPrazo(ChronoUnit.MONTHS.between(YearMonth.from(request.getDataInicio()), YearMonth.from(request.getDataFim())));
        curso.setDataModificacao(LocalDate.now());
        curso.setInstituicaoVinculada(request.getInstituicaoVinculada());
        return curso;
    }

    public static Regencia criarRegencia(RegenciaRequest request, Docente docente) {
        var regencia = new Regencia();
        regencia.setDocente(docente);
        regencia.setProjeto(request.getCurso());
        regencia.setInstituicao(request.getInstituicao());
        regencia.setNivel(request.getNivel());
        regencia.setDisciplinaParticipacao(request.getDisciplinaParticipacao());
        regencia.setResponsavel(request.isResponsavel());
        regencia.setUnicoDocente(request.isUnicoDocente());
        regencia.setValorBruto(request.getValorBrutoTotal());
        regencia.setValorBrutoTotal(regencia.getValorBrutoTotal());
        regencia.setValorBrutoHora(request.getValorBrutoHora());
        regencia.setTotalHorasMinistradas(request.getTotalHorasMinistradas());
        regencia.setTotalHorasOutrasAtividades(request.getTotalHorasOutrasAtividades());
        regencia.setDiasTrabalhadosUnicamp(request.getDiasTrabalhadosUnicamp());
        regencia.setDiasTrabalhadosOutraInstituicao(request.getDiasTrabalhadosOutraInstituicao());
        regencia.setDataFim(request.getDataFim());
        regencia.setDataInicio(request.getDataInicio());
        regencia.setObservacao(request.getObservacao() == null ? "" : request.getObservacao());
        regencia.setPrazo(ChronoUnit.MONTHS.between(YearMonth.from(request.getDataInicio()), YearMonth.from(request.getDataFim())));
        regencia.setDataCriacao(LocalDate.now());
        regencia.setDataModificacao(LocalDate.now());
        regencia.setCoordenador(request.getCoordenador());
        regencia.setHoraMensal(request.getHoraMensal());
        regencia.setHoraSemanal(request.getHoraSemanal());
        regencia.setCurso(request.getCurso());
        regencia.setUrgente(regencia.isUrgente());
        //Mapear request para entidade - mapper struct
        return regencia;
    }

    public static Regencia updateRegencia(RegenciaDto request, Regencia regencia) {
        regencia.setProjeto(request.getCurso());
        regencia.setInstituicao(request.getInstituicao());
        regencia.setNivel(request.getNivel());
        regencia.setDisciplinaParticipacao(request.getDisciplinaParticipacao());
        regencia.setResponsavel(request.isResponsavel());
        regencia.setUnicoDocente(request.isUnicoDocente());
        regencia.setValorBruto(request.getValorBrutoTotal());
        regencia.setValorBrutoTotal(request.getValorBrutoTotal());
        regencia.setValorBrutoHora(request.getValorBrutoHora());
        regencia.setTotalHorasMinistradas(request.getTotalHorasMinistradas());
        regencia.setTotalHorasOutrasAtividades(request.getTotalHorasOutrasAtividades());
        regencia.setDiasTrabalhadosUnicamp(request.getDiasTrabalhadosUnicamp());
        regencia.setDiasTrabalhadosOutraInstituicao(request.getDiasTrabalhadosOutraInstituicao());
        regencia.setDataFim(request.getDataFim());
        regencia.setDataInicio(request.getDataInicio());
        regencia.setObservacao(request.getObservacao() == null ? "" : request.getObservacao());
        regencia.setPrazo(ChronoUnit.MONTHS.between(YearMonth.from(request.getDataInicio()), YearMonth.from(request.getDataFim())));
        regencia.setDataModificacao(LocalDate.now());
        regencia.setCoordenador(request.getCoordenador());
        regencia.setHoraMensal(request.getHoraMensal());
        regencia.setHoraSemanal(request.getHoraSemanal());
        regencia.setCurso(request.getCurso());
        return regencia;
    }

    public static UnivespEntity criarAtividadeUnivesp(UnivespRequest request, Docente docente) {
        var atividadeUnivesp = new UnivespEntity();
        atividadeUnivesp.setDocente(docente);

        //Mapear request para entidade - mapper struct
        return atividadeUnivesp;
    }

    public static StatusAtividade verificaStatusAtividade(Atividade atividade) {
        if (atividade.getDataFim().isBefore(LocalDateTime.now())) {
            return StatusAtividade.CONCLUIDA;
        }

        return StatusAtividade.PENDENTE;
    }
}

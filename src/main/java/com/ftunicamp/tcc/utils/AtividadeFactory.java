package com.ftunicamp.tcc.utils;

import com.ftunicamp.tcc.controllers.request.*;
import com.ftunicamp.tcc.controllers.response.ConvenioDto;
import com.ftunicamp.tcc.controllers.response.CursoExtensaoDto;
import com.ftunicamp.tcc.controllers.response.RegenciaDto;
import com.ftunicamp.tcc.exceptions.NegocioException;
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

    public static Atividade criarConvenio(ConvenioRequest request, Docente docente) {
        if (request.getDataInicio().isAfter(request.getDataFim())) {
            throw new NegocioException("Verificar datas de inicio e fim!");
        }

        var convenio = new ConvenioEntity();
        convenio.setDocente(docente);
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
        convenio.setDataCriacao(LocalDate.now());
        convenio.setDataModificacao(LocalDate.now());
        convenio.setStatus(verificaStatusAtividade(convenio));
        convenio.setObservacao(request.getObservacao() == null ? "" : request.getObservacao());
        convenio.setTipoAtividadeSimultanea(request.getTipoAtividadeSimultanea());
        //Mapear request para entidade - mapper struct
        return convenio;
    }

    public static ConvenioEntity updateConvenio(ConvenioDto request, ConvenioEntity convenio) {
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
        convenio.setStatus(verificaStatusAtividade(convenio));
        convenio.setObservacao(request.getObservacao() == null ? "" : request.getObservacao());
        convenio.setTipoAtividadeSimultanea(request.getTipoAtividadeSimultanea());
        return convenio;
    }

    public static CursoExtensaoEntity criarCurso(CursoExtensaoRequest request, Docente docente) {
        var curso = new CursoExtensaoEntity();
        curso.setDocente(docente);
        curso.setCoordenador(request.getCoordenador());
        curso.setProjeto(request.getNomeCurso());
        curso.setDisciplinaParticipacao(request.getDisciplinas());
        curso.setDataInicio(request.getDataInicio());
        curso.setDataFim(request.getDataFim());
        curso.setValorBruto(request.getValorBrutoTotalAula() + request.getValorBrutoOutraAtividade());
        curso.setValorBrutoOutraAtividade(request.getValorBrutoOutraAtividade());
        curso.setValorBrutoHoraAula(request.getValorBrutoHoraAula());
        curso.setValorBrutoTotalAulas(request.getValorBrutoTotalAula());
        curso.setCargaHorariaTotalMinistrada(request.getCargaHoraTotalMinistrada());
        curso.setHoraMensal(request.getHoraMensal());
        curso.setHoraSemanal(request.getHoraSemanal());
        curso.setParticipacao(Participacao.valueOf(request.getParticipacao()));
        curso.setStatus(verificaStatusAtividade(curso));
        curso.setObservacao(request.getObservacao() == null ? "" : request.getObservacao());
        curso.setPrazo(ChronoUnit.MONTHS.between(YearMonth.from(request.getDataInicio()), YearMonth.from(request.getDataFim())));
        curso.setDataCriacao(LocalDate.now());
        curso.setDataModificacao(LocalDate.now());
        curso.setInstituicaoVinculada(request.getInstituicaoVinculada());
        //Mapear request para entidade - mapper struct
        return curso;
    }

    public static CursoExtensaoEntity updateCurso(CursoExtensaoDto request, CursoExtensaoEntity curso) {
        curso.setCoordenador(request.getCoordenador());
        curso.setProjeto(request.getNomeCurso());
        curso.setDisciplinaParticipacao(request.getDisciplinas());
        curso.setDataInicio(request.getDataInicio());
        curso.setDataFim(request.getDataFim());
        curso.setValorBruto(request.getValorBrutoTotalAula() + request.getValorBrutoOutraAtividade());
        curso.setValorBrutoOutraAtividade(request.getValorBrutoOutraAtividade());
        curso.setValorBrutoHoraAula(request.getValorBrutoHoraAula());
        curso.setValorBrutoTotalAulas(request.getValorBrutoTotalAula());
        curso.setCargaHorariaTotalMinistrada(request.getCargaHorariaTotal());
        curso.setHoraMensal(request.getHoraMensal());
        curso.setHoraSemanal(request.getHoraSemanal());
        curso.setParticipacao(Participacao.valueOf(request.getParticipacao()));
        curso.setStatus(verificaStatusAtividade(curso));
        curso.setObservacao(request.getObservacao() == null ? "" : request.getObservacao());
        curso.setPrazo(ChronoUnit.MONTHS.between(YearMonth.from(request.getDataInicio()), YearMonth.from(request.getDataFim())));
        curso.setDataModificacao(LocalDate.now());
        curso.setInstituicaoVinculada(request.getInstituicaoVinculada());
        return curso;
    }

    public static RegenciaEntity criarRegencia(RegenciaRequest request, Docente docente) {
        var regencia = new RegenciaEntity();
        regencia.setDocente(docente);
        regencia.setProjeto(request.getCurso());
        regencia.setInstituicao(request.getInstituicao());
        regencia.setNivel(request.getNivel());
        regencia.setDisciplinaParticipacao(request.getDisciplinaParticipacao());
        regencia.setResponsavel(request.isResponsavel());
        regencia.setUnicoDocente(request.isUnicoDocente());
        regencia.setCargaHorariaTotalDedicada(request.getCargaHorariaTotalDedicada());
        regencia.setCargaHorariaTotalMinistrada(request.getCargaHoraTotalMinistrada());
        regencia.setValorBrutoOutraAtividade(request.getValorBrutoOutraAtividade());
        regencia.setValorBrutoHoraAula(request.getValorBrutoHoraAula());
        regencia.setValorBrutoTotalAulas(request.getValorBrutoTotalAula());
        regencia.setDiasTrabalhadosUnicamp(request.getDiasTrabalhadosUnicamp());
        regencia.setDiasTrabalhadosOutraInstituicao(request.getDiasTrabalhadosOutraInstituicao());
        regencia.setDataFim(request.getDataFim());
        regencia.setDataInicio(request.getDataInicio());
        regencia.setStatus(verificaStatusAtividade(regencia));
        regencia.setObservacao(request.getObservacao() == null ? "" : request.getObservacao());
        regencia.setPrazo(ChronoUnit.MONTHS.between(YearMonth.from(request.getDataInicio()), YearMonth.from(request.getDataFim())));
        regencia.setDataCriacao(LocalDate.now());
        regencia.setDataModificacao(LocalDate.now());
        regencia.setCoordenador(request.getCoordenador());
        regencia.setHoraMensal(request.getHoraMensal());
        regencia.setHoraSemanal(request.getHoraSemanal());
        regencia.setValorBruto(request.getValorBrutoTotalAula() + request.getValorBrutoOutraAtividade());
        regencia.setCurso(request.getCurso());
        //Mapear request para entidade - mapper struct
        return regencia;
    }

    public static RegenciaEntity updateRegencia(RegenciaDto request, RegenciaEntity regencia) {
        regencia.setProjeto(request.getCurso());
        regencia.setInstituicao(request.getInstituicao());
        regencia.setNivel(request.getNivel());
        regencia.setDisciplinaParticipacao(request.getDisciplinaParticipacao());
        regencia.setResponsavel(request.isResponsavel());
        regencia.setUnicoDocente(request.isUnicoDocente());
        regencia.setCargaHorariaTotalDedicada(request.getCargaHorariaTotalDedicada());
        regencia.setCargaHorariaTotalMinistrada(request.getCargaHoraTotalMinistrada());
        regencia.setValorBrutoOutraAtividade(request.getValorBrutoOutraAtividade());
        regencia.setValorBrutoHoraAula(request.getValorBrutoHoraAula());
        regencia.setValorBrutoTotalAulas(request.getValorBrutoTotalAula());
        regencia.setDiasTrabalhadosUnicamp(request.getDiasTrabalhadosUnicamp());
        regencia.setDiasTrabalhadosOutraInstituicao(request.getDiasTrabalhadosOutraInstituicao());
        regencia.setDataFim(request.getDataFim());
        regencia.setDataInicio(request.getDataInicio());
        regencia.setStatus(verificaStatusAtividade(regencia));
        regencia.setObservacao(request.getObservacao() == null ? "" : request.getObservacao());
        regencia.setPrazo(ChronoUnit.MONTHS.between(YearMonth.from(request.getDataInicio()), YearMonth.from(request.getDataFim())));
        regencia.setDataModificacao(LocalDate.now());
        regencia.setCoordenador(request.getCoordenador());
        regencia.setHoraMensal(request.getHoraMensal());
        regencia.setHoraSemanal(request.getHoraSemanal());
        regencia.setValorBruto(request.getValorBrutoTotalAula() + request.getValorBrutoOutraAtividade());
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

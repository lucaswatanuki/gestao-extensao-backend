package com.ftunicamp.tcc.utils;

import com.ftunicamp.tcc.controllers.request.*;
import com.ftunicamp.tcc.entities.*;
import com.ftunicamp.tcc.exceptions.NegocioException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

@Component
public class AtividadeFactory {

    private AtividadeFactory() {
    }

    public static Atividade criarConvenio(ConvenioRequest request, DocenteEntity docente) {
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
        convenio.setObservacao(request.getObservacao());
        //Mapear request para entidade - mapper struct
        return convenio;
    }

    public static CursoExtensaoEntity criarCurso(CursoExtensaoRequest request, DocenteEntity docente) {
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
        curso.setObservacao(request.getObservacao());
        curso.setPrazo(ChronoUnit.MONTHS.between(YearMonth.from(request.getDataInicio()), YearMonth.from(request.getDataFim())));
        curso.setDataCriacao(LocalDate.now());
        curso.setDataModificacao(LocalDate.now());
        //Mapear request para entidade - mapper struct
        return curso;
    }

    public static RegenciaEntity criarRegencia(RegenciaRequest request, DocenteEntity docente) {
        var regencia = new RegenciaEntity();
        regencia.setDocente(docente);

        //Mapear request para entidade - mapper struct
        return regencia;
    }

    public static UnivespEntity criarAtividadeUnivesp(UnivespRequest request, DocenteEntity docente) {
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

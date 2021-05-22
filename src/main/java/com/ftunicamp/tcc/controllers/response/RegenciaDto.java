package com.ftunicamp.tcc.controllers.response;

import com.ftunicamp.tcc.dto.AlocacaoDto;
import com.ftunicamp.tcc.model.Nivel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RegenciaDto extends AtividadeDetalheResponse {

    private final Nivel nivel;
    private final String curso;
    private final String coordenador;
    private final String disciplinaParticipacao;
    private final int cargaHoraTotalMinistrada;
    private final int cargaHorariaTotalDedicada;
    private final Double valorBrutoHoraAula;
    private final Double valorBrutoTotalAula;
    private final Double valorBrutoOutraAtividade;
    private final String instituicao;
    private final String diasTrabalhadosUnicamp;
    private final String diasTrabalhadosOutraInstituicao;
    private final boolean responsavel;
    private final boolean unicoDocente;

    @Builder
    public RegenciaDto(long id, String projeto, Double valorBruto, String docente, int horaMensal, int horaSemanal, long prazo,
                       LocalDateTime dataInicio, LocalDateTime dataFim, String observacao, boolean autorizado, String tipoAtividade,
                       String revisao, List<AlocacaoDto> alocacoes, boolean excedido, Nivel nivel, String curso, String coordenador, String disciplinaParticipacao,
                       int cargaHoraTotalMinistrada, int cargaHorariaTotalDedicada, Double valorBrutoHoraAula, Double valorBrutoTotalAula,
                       Double valorBrutoOutraAtividade, String instituicao, String diasTrabalhadosUnicamp, String diasTrabalhadosOutraInstituicao,
                       boolean responsavel, boolean unicoDocente) {
        super(id, projeto, valorBruto, docente, horaMensal, horaSemanal, prazo, dataInicio, dataFim, observacao, autorizado, tipoAtividade, revisao, alocacoes, excedido);
        this.nivel = nivel;
        this.curso = curso;
        this.coordenador = coordenador;
        this.disciplinaParticipacao = disciplinaParticipacao;
        this.cargaHoraTotalMinistrada = cargaHoraTotalMinistrada;
        this.cargaHorariaTotalDedicada = cargaHorariaTotalDedicada;
        this.valorBrutoHoraAula = valorBrutoHoraAula;
        this.valorBrutoTotalAula = valorBrutoTotalAula;
        this.valorBrutoOutraAtividade = valorBrutoOutraAtividade;
        this.instituicao = instituicao;
        this.diasTrabalhadosUnicamp = diasTrabalhadosUnicamp;
        this.diasTrabalhadosOutraInstituicao = diasTrabalhadosOutraInstituicao;
        this.responsavel = responsavel;
        this.unicoDocente = unicoDocente;
    }
}

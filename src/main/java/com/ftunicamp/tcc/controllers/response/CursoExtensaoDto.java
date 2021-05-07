package com.ftunicamp.tcc.controllers.response;

import com.ftunicamp.tcc.dto.AlocacaoDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CursoExtensaoDto extends AtividadeDetalheResponse {

    private final String participacao;
    private final String disciplinas;
    private final int cargaHorariaTotal;
    private final double valorBrutoHoraAula;
    private final double valorBrutoTotalAula;
    private final double valorBrutoOutraAtividade;
    private final int periodo;
    private final String nomeCurso;
    private final String instituicaoVinculada;
    private final String coordenador;

    @Builder
    public CursoExtensaoDto(long id, String projeto, Double valorBruto, String docente, int horaMensal,
                            int horaSemanal, long prazo, LocalDateTime dataInicio, LocalDateTime dataFim, String observacao, boolean autorizado, String tipoAtividade, String revisao,
                            List<AlocacaoDto> alocacoes, String participacao, String disciplinas, int cargaHorariaTotal,
                            double valorBrutoHoraAula, double valorBrutoTotalAula, double valorBrutoOutraAtividade,
                            int periodo, String nomeCurso, String instituicaoVinculada, String coordenador) {
        super(id, projeto, valorBruto, docente, horaMensal, horaSemanal, prazo, dataInicio, dataFim,
                observacao, autorizado, tipoAtividade, revisao, alocacoes);
        this.participacao = participacao;
        this.disciplinas = disciplinas;
        this.cargaHorariaTotal = cargaHorariaTotal;
        this.valorBrutoHoraAula = valorBrutoHoraAula;
        this.valorBrutoTotalAula = valorBrutoTotalAula;
        this.valorBrutoOutraAtividade = valorBrutoOutraAtividade;
        this.periodo = periodo;
        this.nomeCurso = nomeCurso;
        this.instituicaoVinculada = instituicaoVinculada;
        this.coordenador = coordenador;
    }
}

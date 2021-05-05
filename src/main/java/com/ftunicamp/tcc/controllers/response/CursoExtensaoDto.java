package com.ftunicamp.tcc.controllers.response;

import com.ftunicamp.tcc.dto.AlocacaoDto;
import com.ftunicamp.tcc.model.Alocacao;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CursoExtensaoDto extends AtividadeDetalheResponse {

    private final String participacao;
    private final String disciplinas;
    private final int cargaHorariaTotal;
    private final double valorBrutoHoraAula;
    private final double valorBrutoTotalAula;
    private final double valorBrutoOutrasAtividade;
    private final int periodo;
    private final String nomeCurso;
    private final String instituicaoVinculada;

    @Builder
    public CursoExtensaoDto(long id, String projeto, Double valorBruto, String docente, int horaMensal,
                            int horaSemanal, long prazo, String dataInicio, String dataFim, String observacao, boolean autorizado, String tipoAtividade, String revisao,
                            List<AlocacaoDto> alocacoes, String participacao, String disciplinas, int cargaHorariaTotal,
                            double valorBrutoHoraAula, double valorBrutoTotalAula, double valorBrutoOutrasAtividade,
                            int periodo, String nomeCurso, String instituicaoVinculada) {
        super(id, projeto, valorBruto, docente, horaMensal, horaSemanal, prazo, dataInicio, dataFim,
                observacao, autorizado, tipoAtividade, revisao, alocacoes);
        this.participacao = participacao;
        this.disciplinas = disciplinas;
        this.cargaHorariaTotal = cargaHorariaTotal;
        this.valorBrutoHoraAula = valorBrutoHoraAula;
        this.valorBrutoTotalAula = valorBrutoTotalAula;
        this.valorBrutoOutrasAtividade = valorBrutoOutrasAtividade;
        this.periodo = periodo;
        this.nomeCurso = nomeCurso;
        this.instituicaoVinculada = instituicaoVinculada;
    }
}

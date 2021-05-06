package com.ftunicamp.tcc.controllers.response;

import com.ftunicamp.tcc.dto.AlocacaoDto;
import com.ftunicamp.tcc.model.Alocacao;
import lombok.*;

import java.util.List;

@Getter
public class ConvenioDto extends AtividadeDetalheResponse{
    private final String descricao;
    private final String instituicao;
    private final String tipoAtividadeSimultanea;


    @Builder
    public ConvenioDto(long id, String projeto, Double valorBruto, String docente, int horaMensal, int horaSemanal, long prazo, String dataInicio, String dataFim, String observacao, boolean autorizado, String tipoAtividade, String revisao, List<AlocacaoDto> alocacoes, String descricao, String instituicao, String tipoAtividadeSimultanea) {
        super(id, projeto, valorBruto, docente, horaMensal, horaSemanal, prazo, dataInicio, dataFim, observacao, autorizado, tipoAtividade, revisao, alocacoes);
        this.descricao = descricao;
        this.instituicao = instituicao;
        this.tipoAtividadeSimultanea = tipoAtividadeSimultanea;
    }
}

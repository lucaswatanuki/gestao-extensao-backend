package com.ftunicamp.tcc.controllers.response;

import com.ftunicamp.tcc.dto.AlocacaoDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ConvenioDto extends AtividadeDetalheResponse{
    private final String descricao;
    private final String instituicao;
    private final String tipoAtividadeSimultanea;
    private final String coordenador;


    @Builder
    public ConvenioDto(long id, String projeto, Double valorBruto, String docente, int horaMensal, int horaSemanal, long prazo,
                       LocalDateTime dataInicio, LocalDateTime dataFim, String observacao, boolean autorizado, String tipoAtividade, String revisao,
                       List<AlocacaoDto> alocacoes, String descricao, String instituicao, String tipoAtividadeSimultanea, String coordenador) {
        super(id, projeto, valorBruto, docente, horaMensal, horaSemanal, prazo, dataInicio, dataFim, observacao, autorizado, tipoAtividade, revisao, alocacoes);
        this.descricao = descricao;
        this.instituicao = instituicao;
        this.tipoAtividadeSimultanea = tipoAtividadeSimultanea;
        this.coordenador = coordenador;
    }
}

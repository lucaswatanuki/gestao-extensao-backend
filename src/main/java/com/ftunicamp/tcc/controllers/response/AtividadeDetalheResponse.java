package com.ftunicamp.tcc.controllers.response;

import com.ftunicamp.tcc.dto.AlocacaoDto;
import com.ftunicamp.tcc.model.Alocacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AtividadeDetalheResponse {

    private final long id;
    private final String projeto;
    private final Double valorBruto;
    private final String docente;
    private final int horaMensal;
    private final int horaSemanal;
    private final long prazo;
    private final String dataInicio;
    private final String dataFim;
    private final String observacao;
    private final boolean autorizado;
    private final String tipoAtividade;
    private final String revisao;
    private final List<AlocacaoDto> alocacoes;

}

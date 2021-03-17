package com.ftunicamp.tcc.controllers.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AtividadeDetalheResponse {

    private long id;
    private String projeto;
    private Double valorBruto;
    private String docente;
    private int horaMensal;
    private int horaSemanal;
    private long prazo;
    private String dataInicio;
    private String dataFim;
    private long horasAprovadas;
    private long horasSolicitadas;
    private String observacao;

}

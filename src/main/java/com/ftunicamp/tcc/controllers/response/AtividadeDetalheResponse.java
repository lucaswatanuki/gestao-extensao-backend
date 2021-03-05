package com.ftunicamp.tcc.controllers.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
    private long horasEmAndamento;
    private long horasFuturas;

}

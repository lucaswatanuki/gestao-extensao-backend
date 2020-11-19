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
    private int prazo;

}

package com.ftunicamp.tcc.controllers.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocenteResponse {

    private String rf;
    private String nome;
    private String email;
    private Boolean autorizado;
    private long totalHorasEmAndamento;
    private long totalHorasFuturas;
}

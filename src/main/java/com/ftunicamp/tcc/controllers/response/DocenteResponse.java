package com.ftunicamp.tcc.controllers.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocenteResponse {

    private long id;
    private String matricula;
    private String nome;
    private String email;
    private Boolean autorizado;
    private long totalHoras;
    private long totalHorasAprovadas;
    private long totalHorasSolicitadas;
}

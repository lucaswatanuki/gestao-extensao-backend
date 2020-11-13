package com.ftunicamp.tcc.controllers.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class AtividadeResponse implements Serializable {

    private Long id;
    private LocalDate dataCriacao;
    private int prazo;
    private String projeto;

}

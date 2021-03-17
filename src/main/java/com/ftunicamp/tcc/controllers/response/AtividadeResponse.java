package com.ftunicamp.tcc.controllers.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class AtividadeResponse implements Serializable {

    private Long id;
    private LocalDate dataCriacao;
    private long prazo;
    private String projeto;

}

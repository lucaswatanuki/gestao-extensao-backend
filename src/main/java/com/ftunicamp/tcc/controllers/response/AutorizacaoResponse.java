package com.ftunicamp.tcc.controllers.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class AutorizacaoResponse implements Serializable {

    private long id;

    private String status;

    private String dataCriacao;

    private String docente;

    private long horas;

    private boolean urgente;

}

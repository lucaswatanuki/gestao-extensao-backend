package com.ftunicamp.tcc.controllers.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AutorizacaoResponse implements Serializable {

    private long id;

    private String status;

    private String dataCriacao;

    private String docente;

    private int horas;

    private boolean urgente;

}

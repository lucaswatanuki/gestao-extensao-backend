package com.ftunicamp.tcc.entities;

import lombok.Getter;

@Getter
public enum StatusAutorizacao {

    A("Aprovado"),
    P("Pendente"),
    R("Reprovado");

    private final String status;

    StatusAutorizacao(String status) {
        this.status = status;
    }
}

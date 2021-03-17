package com.ftunicamp.tcc.entities;

import lombok.Getter;

@Getter
public enum StatusAutorizacao {

    APROVADO("Aceito"),
    PENDENTE("Pendente"),
    REPROVADO("Reprovado");

    private final String status;

    StatusAutorizacao(String status) {
        this.status = status;
    }
}

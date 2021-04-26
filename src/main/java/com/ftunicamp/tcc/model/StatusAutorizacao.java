package com.ftunicamp.tcc.model;

import lombok.Getter;

@Getter
public enum StatusAutorizacao {

    APROVADO("Aceito"),
    PENDENTE("Pendente"),
    REPROVADO("Reprovado"),
    REVISAO("Em revisão");


    private final String status;

    StatusAutorizacao(String status) {
        this.status = status;
    }
}

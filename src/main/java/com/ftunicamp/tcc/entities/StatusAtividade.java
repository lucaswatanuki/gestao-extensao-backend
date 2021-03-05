package com.ftunicamp.tcc.entities;

import lombok.Getter;

@Getter
public enum StatusAtividade {
    CONCLUIDA("Concluida"), EM_ANDAMENTO("Em andamento"), FUTURA("Aguardando aceite"), TODOS("Todos");

    private final String status;

    StatusAtividade(String status) {
        this.status = status;
    }
}

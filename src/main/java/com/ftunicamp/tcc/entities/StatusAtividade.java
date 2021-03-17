package com.ftunicamp.tcc.entities;

import lombok.Getter;

@Getter
public enum StatusAtividade {
    CONCLUIDA("Concluida"), EM_ANDAMENTO("Em andamento"), PENDENTE("Aguardando aceite"), EM_REVISAO("Em revisão"), TODOS("Todos");

    private final String status;

    StatusAtividade(String status) {
        this.status = status;
    }
}

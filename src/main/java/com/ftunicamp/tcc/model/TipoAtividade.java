package com.ftunicamp.tcc.model;

import lombok.Getter;

@Getter
public enum TipoAtividade {

    CURSO("curso"), CONVENIO("convenio"), REGENCIA("regencia");

    private final String value;

    TipoAtividade(String value) {
        this.value = value;
    }
}

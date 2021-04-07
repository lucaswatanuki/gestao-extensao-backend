package com.ftunicamp.tcc.utils;

import lombok.Getter;

@Getter
public enum TipoPDF {
    CURSO("curso"), CONVENIO("convenio"), REGENCIA("regencia");

    private final String value;

    TipoPDF(String value) {
        this.value = value;
    }
}

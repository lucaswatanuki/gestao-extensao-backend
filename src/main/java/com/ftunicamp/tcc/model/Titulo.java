package com.ftunicamp.tcc.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Titulo {
    NENHUM("Nenhum"),MS31("MS-3.1"), MS32("MS-3.2"), MS51("MS-5.1"), MS52("MS-5.2"), MS53("MS-5.3"), MS6("MS-6");

    private final String value;

    Titulo(String value) {
        this.value = value;
    }

    public static Titulo fromString(String titulo) {
        return Arrays.stream(Titulo.values())
                .filter(t -> t.getValue().equalsIgnoreCase(titulo))
                .findAny()
                .orElse(null);
    }
}

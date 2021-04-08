package com.ftunicamp.tcc.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContextPdf {
    private String nomeCoordenador;
    private int dia;
    private String mes;
    private int ano;
}

package com.ftunicamp.tcc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ArquivoDto {
    private String nome;
    private String url;
    private String tipo;
    private long tamanho;
}

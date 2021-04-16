package com.ftunicamp.tcc.controllers.request;

import lombok.Data;

@Data
public class AutorizacaoRequest {
    private String observacao;
    private boolean autorizado;
}

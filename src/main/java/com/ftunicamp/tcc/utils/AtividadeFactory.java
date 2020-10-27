package com.ftunicamp.tcc.utils;

import com.ftunicamp.tcc.controllers.request.AtividadeRequest;
import com.ftunicamp.tcc.entities.AtividadeEntity;
import org.springframework.stereotype.Component;

@Component
public class AtividadeFactory {

    private AtividadeFactory() {
    }

    public static AtividadeEntity criarAtividade(AtividadeRequest request) {
        var atividade = new AtividadeEntity();
        return atividade;
    }
}

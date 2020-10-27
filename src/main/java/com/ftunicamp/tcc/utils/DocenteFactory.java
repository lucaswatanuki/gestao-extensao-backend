package com.ftunicamp.tcc.utils;

import com.ftunicamp.tcc.controllers.request.SignUpRequest;
import com.ftunicamp.tcc.entities.DocenteEntity;
import org.springframework.stereotype.Component;

@Component
public class DocenteFactory {

    private DocenteFactory() {
    }

    public static DocenteEntity criarDocente(SignUpRequest request) {
        var docente = new DocenteEntity();
        docente.setNome(request.getNomeCompleto());
        docente.setCpf(request.getCpf());
        docente.setEmail(request.getEmail());
        docente.setEndereco(request.getEndereco());
        docente.setRf(request.getRf());
        docente.setTelefone(request.getTelefone());

        return docente;
    }
}

package com.ftunicamp.tcc.utils;

import com.ftunicamp.tcc.controllers.request.SignUpRequest;
import com.ftunicamp.tcc.entities.DocenteEntity;
import com.ftunicamp.tcc.entities.UsuarioEntity;
import org.springframework.stereotype.Component;

@Component
public class DocenteFactory {

    private DocenteFactory() {
    }

    public static DocenteEntity criarDocente(SignUpRequest request, UsuarioEntity user) {

        var docente = new DocenteEntity();
        docente.setNome(request.getNome());
        docente.setCpf(request.getCpf().replaceAll("[^0-9]",""));
        docente.setEmail(request.getEmail());
        docente.setEndereco(request.getEndereco());
        docente.setRf(request.getRf());
        docente.setTelefone(request.getTelefone().replaceAll("[^0-9]",""));
        docente.setUser(user);

        return docente;
    }
}

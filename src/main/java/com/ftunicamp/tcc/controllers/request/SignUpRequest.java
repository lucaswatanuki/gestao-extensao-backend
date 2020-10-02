package com.ftunicamp.tcc.controllers.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpRequest {

    private String username;

    private String nomeCompleto;

    private String cpf;

    private String rf;

    private String endereco;

    private String email;

    private String telefone;

    private LocalDate dtNascimento;

    private Set<String> profile;

    private String password;
}

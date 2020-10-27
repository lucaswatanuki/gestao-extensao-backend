package com.ftunicamp.tcc.controllers.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignUpRequest {

    @JsonProperty("usuario")
    private String username;

    @JsonProperty("nome_completo")
    private String nomeCompleto;

    private String cpf;

    private String rf;

    private String endereco;

    private String email;

    private String telefone;

    @JsonProperty("perfil_acesso")
    private Set<String> profile;

    @JsonProperty("senha")
    private String password;
}

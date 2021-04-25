package com.ftunicamp.tcc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioDto {

    private String nome;
    private String senhaAtual;
    private String senhaNova;
    private String senhaConfirmacao;
    private String email;
    private String telefone;
    private String matricula;

}

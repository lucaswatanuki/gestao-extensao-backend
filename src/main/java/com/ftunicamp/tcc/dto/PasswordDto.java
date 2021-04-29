package com.ftunicamp.tcc.dto;

import lombok.Data;

@Data
public class PasswordDto {

    private String email;
    private String token;
    private String senha;
    private String senhaAtual;

}

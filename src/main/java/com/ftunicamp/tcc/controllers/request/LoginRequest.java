package com.ftunicamp.tcc.controllers.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    private String matricula;

    private String password;
}

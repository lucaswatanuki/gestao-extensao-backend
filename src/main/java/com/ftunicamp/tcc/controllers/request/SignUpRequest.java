package com.ftunicamp.tcc.controllers.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignUpRequest {

    private String username;

    private String email;

    private Set<String> profile;

    private String password;
}

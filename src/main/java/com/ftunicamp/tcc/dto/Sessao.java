package com.ftunicamp.tcc.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Getter
@Setter
public class Sessao {

    private String username;
    private List<String> profiles;
}

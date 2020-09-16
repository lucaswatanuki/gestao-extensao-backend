package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.request.LoginRequest;
import com.ftunicamp.tcc.controllers.request.SignUpRequest;
import com.ftunicamp.tcc.controllers.response.JwtResponse;

public interface AutenticacaoService {

    JwtResponse autenticarUsuario(LoginRequest loginRequest);

    String registrarUsuario(SignUpRequest signUpRequest);
}

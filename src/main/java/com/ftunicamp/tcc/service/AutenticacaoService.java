package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.request.LoginRequest;
import com.ftunicamp.tcc.controllers.request.SignUpRequest;
import com.ftunicamp.tcc.controllers.response.JwtResponse;
import com.ftunicamp.tcc.entities.ProfilesEntity;

import java.util.List;

public interface AutenticacaoService {

    JwtResponse autenticarUsuario(LoginRequest loginRequest);

    String registrarUsuario(SignUpRequest signUpRequest);

    List<ProfilesEntity> registrarPerfilAcesso();
}

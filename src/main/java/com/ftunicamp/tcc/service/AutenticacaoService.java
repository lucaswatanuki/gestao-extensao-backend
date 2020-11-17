package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.controllers.request.LoginRequest;
import com.ftunicamp.tcc.controllers.request.SignUpRequest;
import com.ftunicamp.tcc.controllers.response.JwtResponse;
import com.ftunicamp.tcc.entities.ProfilesEntity;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface AutenticacaoService {

    JwtResponse autenticarUsuario(LoginRequest loginRequest);

    String registrarUsuario(SignUpRequest signUpRequest, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException;

    boolean verificarUsuario(String codigoVerificacao);

    List<ProfilesEntity> registrarPerfilAcesso();
}

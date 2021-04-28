package com.ftunicamp.tcc.service;


import com.ftunicamp.tcc.dto.PasswordDto;
import com.ftunicamp.tcc.model.UsuarioEntity;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface PasswordService {

    void resetarSenha(PasswordDto dto, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException;
    void alterarSenha(PasswordDto dto);
    Boolean validarToken(String token);

}

package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.model.Atividade;
import com.ftunicamp.tcc.model.AutorizacaoEntity;
import com.ftunicamp.tcc.model.DocenteEntity;
import com.ftunicamp.tcc.model.UsuarioEntity;
import com.ftunicamp.tcc.utils.TipoEmail;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {

    void enviarEmailVerificacao(DocenteEntity docente, String baseUrl) throws UnsupportedEncodingException, MessagingException;

    void enviarEmailAtividade(Atividade atividade, TipoEmail tipoEmail, String observacao) throws MessagingException, UnsupportedEncodingException;

    void enviarEmailResetSenha(UsuarioEntity user, String token, String baseUrl) throws MessagingException, UnsupportedEncodingException;
}

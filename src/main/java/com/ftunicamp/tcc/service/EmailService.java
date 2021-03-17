package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.entities.DocenteEntity;
import com.ftunicamp.tcc.utils.TipoEmail;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {

    void enviarEmailVerificacao(DocenteEntity docente, String baseUrl) throws UnsupportedEncodingException, MessagingException;

    void enviarEmailAtividade(DocenteEntity docente, TipoEmail tipoEmail, long atividadeId, String observacao) throws MessagingException, UnsupportedEncodingException;
}

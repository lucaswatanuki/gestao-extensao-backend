package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.config.EmailConfiguration;
import com.ftunicamp.tcc.model.Atividade;
import com.ftunicamp.tcc.model.Docente;
import com.ftunicamp.tcc.model.UsuarioEntity;
import com.ftunicamp.tcc.service.EmailService;
import com.ftunicamp.tcc.utils.TipoEmail;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailConfiguration emailConfig;
    private static final String REMETENTE = "Coordenadoria de Extensão FT ";
    private static final String SUBJECT = "subject";
    private static final String TEMPLATE = "template";
    private static final String TRACKING = "o:tracking";

    @Value("${spring.mail.from.username}")
    String email;

    @Value("${frontend.url}")
    String frontUrl;

    @Autowired
    public EmailServiceImpl(EmailConfiguration emailConfig) {
        this.emailConfig = emailConfig;
    }

    @Override
    @Async
    public void enviarEmailVerificacao(Docente docente, String baseUrl) throws UnsupportedEncodingException {
        var assunto = "Confirmação de cadastro";
        String urlVerificada = baseUrl + "/auth/confirmacao?codigo=" + docente.getUser().getCodigoVerificacao();

        try {
            var request = Unirest.post("https://api.mailgun.net/v3/" + emailConfig.getDomain() + "/messages")
                    .basicAuth("api", emailConfig.getApiKey())
                    .field("from", REMETENTE + email)
                    .field("to", docente.getEmail())
                    .field(SUBJECT, assunto)
                    .field(TEMPLATE, "template_cadastro")
                    .field(TRACKING, "False")
                    .field("v:docente", docente.getNome())
                    .field("v:url", urlVerificada)
                    .asJson();
            Logger.getAnonymousLogger().log(Level.INFO, "{0}", request.getBody().toPrettyString());
        } catch (UnirestException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }

    }

    @Override
    @Async
    public void enviarEmailAtividade(Atividade atividade, TipoEmail tipoEmail, String observacao) {
        var body = "";
        var assunto = "";
        var docente = atividade.getDocente();

        if (tipoEmail.equals(TipoEmail.NOVA_ATIVIDADE)) {
            assunto = "Atividade submetida";
            body += "Sua solicitação de atividade #" + atividade.getId() + " foi submetida com sucesso e encaminhada para a Coordenadoria de Extensão.";
        } else if (tipoEmail.equals(TipoEmail.STATUS_ATIVIDADE)) {
            assunto = "Atualização de status da atividade";
            body += "O status da sua atividade foi alterado para: " + atividade.getStatus().getStatus();
        }

        try {
            var request = Unirest.post("https://api.mailgun.net/v3/" + emailConfig.getDomain() + "/messages")
                    .basicAuth("api", emailConfig.getApiKey())
                    .field("from", REMETENTE + email)
                    .field("to", docente.getEmail())
                    .field(SUBJECT, assunto)
                    .field(TEMPLATE, tipoEmail.equals(TipoEmail.NOVA_ATIVIDADE) ? "template_atividade": "atividade_update")
                    .field(TRACKING, "False")
                    .field("v:docente", docente.getNome())
                    .field("v:body", body)
                    .field("v:observacao", (observacao == null || observacao.isEmpty()) ? "-" : observacao)
                    .asJson();
            Logger.getAnonymousLogger().log(Level.INFO, "{0}", request.getBody().toPrettyString());
        } catch (UnirestException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    public void enviarEmailResetSenha(UsuarioEntity user, String token, String baseUrl) {
        var assunto = "Alterar senha";
        String urlVerificada = frontUrl + "senha/alterarSenha/" + token;

        try {
            var request = Unirest.post("https://api.mailgun.net/v3/" + emailConfig.getDomain() + "/messages")
                    .basicAuth("api", emailConfig.getApiKey())
                    .field("from", REMETENTE + email)
                    .field("to", user.getEmail())
                    .field(SUBJECT, assunto)
                    .field(TEMPLATE, "reset_senha")
                    .field(TRACKING, "False")
                    .field("v:username", user.getUsername())
                    .field("v:url", urlVerificada)
                    .asJson();
            Logger.getAnonymousLogger().log(Level.INFO, "{0}", request.getBody().toPrettyString());
        } catch (UnirestException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }
    }
}

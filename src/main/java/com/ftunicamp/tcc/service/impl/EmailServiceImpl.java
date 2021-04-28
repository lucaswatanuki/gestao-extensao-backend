package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.config.EmailConfiguration;
import com.ftunicamp.tcc.model.DocenteEntity;
import com.ftunicamp.tcc.model.UsuarioEntity;
import com.ftunicamp.tcc.service.EmailService;
import com.ftunicamp.tcc.utils.TipoEmail;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailConfiguration emailConfig;

    private final String DOMAIN_NAME = "sandboxe5aae7addd594220951e601477f30e32.mailgun.org";

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
    public void enviarEmailVerificacao(DocenteEntity docente, String baseUrl) throws UnsupportedEncodingException, MessagingException {
        String assunto = "Confirmação de cadastro";
        String remetente = "Coordenadoria de Extensão FT ";
        String urlVerificada = baseUrl + "/auth/confirmacao?codigo=" + docente.getUser().getCodigoVerificacao();

        try {
            var request = Unirest.post("https://api.mailgun.net/v3/" + emailConfig.getDomain() + "/messages")
                    .basicAuth("api", emailConfig.getApiKey())
                    .field("from", remetente + email)
                    .field("to", docente.getEmail())
                    .field("subject", assunto)
                    .field("template", "template_cadastro")
                    .field("o:tracking", "False")
                    .field("v:docente", docente.getNome())
                    .field("v:url", urlVerificada)
                    .asJson();
            Logger.getAnonymousLogger().log(Level.INFO, request.getBody().toPrettyString());
        } catch (UnirestException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }

    }

    @Override
    @Async
    public void enviarEmailAtividade(DocenteEntity docente, TipoEmail tipoEmail, long atividadeId, String observacao) throws MessagingException, UnsupportedEncodingException {
        String remetente = "Coordenadoria de Extensão FT ";
        String body = "";
        String assunto = "";

        if (tipoEmail.equals(TipoEmail.NOVA_ATIVIDADE)) {
            assunto = "Atividade submetida";
            body += "Sua solicitação de atividade #" + atividadeId + " foi submetida com sucesso e encaminhada para a Coordenadoria de Extensão.";
        } else if (tipoEmail.equals(TipoEmail.STATUS_ATIVIDADE)) {
            assunto = "Atualização de status da atividade";
            body += "O status da sua atividade foi alterado.";
            if (!observacao.isEmpty()) {
                body += "<p>Observações: </p>";
                body += observacao;
            }
        }

        try {
            var request = Unirest.post("https://api.mailgun.net/v3/" + emailConfig.getDomain() + "/messages")
                    .basicAuth("api", emailConfig.getApiKey())
                    .field("from", remetente + email)
                    .field("to", docente.getEmail())
                    .field("subject", assunto)
                    .field("template", "template_atividade")
                    .field("o:tracking", "False")
                    .field("v:docente", docente.getNome())
                    .field("v:body", body)
                    .asJson();
            Logger.getAnonymousLogger().log(Level.INFO, request.getBody().toPrettyString());
        } catch (UnirestException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }
    }

    @Override
    public void enviarEmailResetSenha(UsuarioEntity user, String token, String baseUrl) throws UnsupportedEncodingException {
        String assunto = "Alterar senha";
        String remetente = "Coordenadoria de Extensão FT ";
        String urlVerificada = baseUrl + "/senha/alterarSenha?token=" + token;

        try {
            var request = Unirest.post("https://api.mailgun.net/v3/" + emailConfig.getDomain() + "/messages")
                    .basicAuth("api", emailConfig.getApiKey())
                    .field("from", remetente + email)
                    .field("to", user.getEmail())
                    .field("subject", assunto)
                    .field("template", "reset_senha")
                    .field("o:tracking", "False")
                    .field("v:username", user.getUsername())
                    .field("v:url", urlVerificada)
                    .asJson();
            Logger.getAnonymousLogger().log(Level.INFO, request.getBody().toPrettyString());
        } catch (UnirestException e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }
    }

    private JavaMailSenderImpl getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailConfig.getHost());
        mailSender.setPort(this.emailConfig.getPort());
        mailSender.setUsername(this.emailConfig.getUsername());
        mailSender.setPassword(this.emailConfig.getPassword());
        return mailSender;
    }
}

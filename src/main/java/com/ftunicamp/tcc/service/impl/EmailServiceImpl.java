package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.entities.DocenteEntity;
import com.ftunicamp.tcc.service.EmailService;
import com.ftunicamp.tcc.utils.TipoEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    String email;

    @Override
    @Async
    public void enviarEmailVerificacao(DocenteEntity docente, String baseUrl) throws UnsupportedEncodingException, MessagingException {
        String assunto = "Confirmação de cadastro";
        String remetente = "Coordenadoria de Extensão FT";
        String body = "<p>Prezado(a) " + docente.getNome() + ",</p>";
        body += "<p>Por favor, clique no link abaixo para confirmar seu cadastro</p>";
        String urlVerificada = baseUrl + "/auth/confirmacao?codigo=" + docente.getUser().getCodigoVerificacao();
        body += "<h3><a href=\"" + urlVerificada + "\">VERIFICAR</a></h3>";
        body += "<p>Atenciosamente,<br>Coordenadoria de Extensão FT</p>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(email, remetente);
        helper.setTo(docente.getEmail());
        helper.setSubject(assunto);
        helper.setText(body, true);

        javaMailSender.send(message);

    }

    @Override
    @Async
    public void enviarEmailAtividade(DocenteEntity docente, TipoEmail tipoEmail) throws MessagingException, UnsupportedEncodingException {
        String remetente = "Coordenadoria de Extensão FT";
        String body = "";
        String assunto = "";

        if (tipoEmail.equals(TipoEmail.NOVA_ATIVIDADE)) {
            assunto = "Atividade submetida";
            body = "<p>Prezado(a) " + docente.getNome() + ",</p>";
            body += "<p>Sua solicitação de atividade foi submetida com sucesso e encaminhada para a Coordenadoria de Extensão.</p>";
            body += "<p>Atenciosamente,<br>Coordenadoria de Extensão FT</p>";
        } else if (tipoEmail.equals(TipoEmail.STATUS_ATIVIDADE)) {
            assunto = "Atualização de status da atividade";
            body = "<p>Prezado(a) " + docente.getNome() + ",</p>";
            body += "<p>O status da sua atividade foi alterado.</p>";
            body += "<p>Atenciosamente,<br>Coordenadoria de Extensão FT</p>";
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(email, remetente);
        helper.setTo(docente.getEmail());
        helper.setSubject(assunto);
        helper.setText(body, true);

        javaMailSender.send(message);
    }
}

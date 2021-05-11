package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.dto.PasswordDto;
import com.ftunicamp.tcc.exceptions.NegocioException;
import com.ftunicamp.tcc.model.PasswordResetToken;
import com.ftunicamp.tcc.model.UsuarioEntity;
import com.ftunicamp.tcc.repositories.PasswordTokenRepository;
import com.ftunicamp.tcc.repositories.UserRepository;
import com.ftunicamp.tcc.security.jwt.JwtUtils;
import com.ftunicamp.tcc.service.EmailService;
import com.ftunicamp.tcc.service.PasswordService;
import com.ftunicamp.tcc.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordTokenRepository tokenRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordServiceImpl(EmailService emailService, UserRepository userRepository, PasswordTokenRepository tokenRepository, JwtUtils jwtUtils) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void resetarSenha(PasswordDto dto, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        var user = userRepository.findByEmail(dto.getEmail());

        if (user == null) {
            throw new NoSuchElementException("User not found");
        }

        String token = UUID.randomUUID().toString();
        createPasswordResetTokenForUser(user, token);
        var baseUrl = Utilities.getBaseUrl(request);

        emailService.enviarEmailResetSenha(user, token, baseUrl);
    }

    @Override
    public void alterarSenha(PasswordDto dto) {
        var userToken = new PasswordResetToken();
        var usuario = new UsuarioEntity();

        if (jwtUtils.getSessao().getUsername().isEmpty()){
            userToken = tokenRepository.findByToken(dto.getToken());
            if (userToken == null) {
                throw new NoSuchElementException("Usuario nao encontrado");
            }
            usuario = userToken.getUser();
        } else {
            usuario = userRepository.findByUsername(jwtUtils.getSessao().getUsername()).orElse(null);
        }

        if (usuario == null) {
            throw new NoSuchElementException("Usuário não encontrado");
        }

        if (dto.getSenhaAtual() != null) {
            if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getPassword())) {
                throw new NegocioException("Verificar senha");
            }
        }
        usuario.setPassword(passwordEncoder.encode(dto.getSenha()));

        userRepository.save(usuario);
    }

    @Override
    public Boolean validarToken(String token) {
        var passToken = tokenRepository.findByToken(token);

        return isTokenFound(passToken);
    }

    public void createPasswordResetTokenForUser(UsuarioEntity user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        tokenRepository.save(myToken);
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }


}

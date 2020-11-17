package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.request.LoginRequest;
import com.ftunicamp.tcc.controllers.request.SignUpRequest;
import com.ftunicamp.tcc.controllers.response.JwtResponse;
import com.ftunicamp.tcc.entities.DocenteEntity;
import com.ftunicamp.tcc.entities.Profiles;
import com.ftunicamp.tcc.entities.ProfilesEntity;
import com.ftunicamp.tcc.entities.UsuarioEntity;
import com.ftunicamp.tcc.exceptions.NegocioException;
import com.ftunicamp.tcc.repositories.DocenteRepository;
import com.ftunicamp.tcc.repositories.ProfilesRepository;
import com.ftunicamp.tcc.repositories.UserRepository;
import com.ftunicamp.tcc.security.jwt.JwtUtils;
import com.ftunicamp.tcc.security.services.UserDetailsImpl;
import com.ftunicamp.tcc.service.AutenticacaoService;
import com.ftunicamp.tcc.utils.DocenteFactory;
import com.ftunicamp.tcc.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class AutenticacaoServiceImpl implements AutenticacaoService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProfilesRepository profilesRepository;

    @Autowired
    private DocenteRepository docenteRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Value("${spring.mail.username}")
    String email;

    @Override
    public JwtResponse autenticarUsuario(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(authentication, userDetails);

        List<String> profiles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        var user = userRepository.findByUsername(userDetails.getUsername());

        user.ifPresent(usuarioEntity -> {
            if (!usuarioEntity.isVerificado()) {
                throw new NegocioException("Usuário não verificado.");
            }
        });


        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                profiles);
    }

    @Override
    public String registrarUsuario(SignUpRequest signUpRequest, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return "Usuário existente";
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return "Email já está em uso.";
        }

        UsuarioEntity user = new UsuarioEntity(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strProfiles = signUpRequest.getProfile();
        Set<ProfilesEntity> roles = new HashSet<>();

        if (strProfiles == null) {
            var userRole = profilesRepository.findByName(Profiles.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strProfiles.forEach(profile -> {
                if (profile.equalsIgnoreCase("admin")) {
                    var adminRole = profilesRepository.findByName(Profiles.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));
                    roles.add(adminRole);
                } else {
                    var userRole = profilesRepository.findByName(Profiles.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));
                    roles.add(userRole);
                }
            });
        }

        user.setProfiles(roles);

        user.setCodigoVerificacao(UUID.randomUUID().toString());

        userRepository.save(user);

        var docente = salvarDocente(DocenteFactory.criarDocente(signUpRequest, user));
        var baseUrl = Utilities.getBaseUrl(request);
        CompletableFuture.runAsync(() -> {
            try {
                enviarEmailVerificacao(user, docente, baseUrl);
            } catch (UnsupportedEncodingException | MessagingException e) {
                e.printStackTrace();
            }
        });

        return "Usuário registrado com sucesso!";
    }

    @Override
    public boolean verificarUsuario(String codigoVerificacao) {
        var usuario = userRepository.findByCodigoVerificacao(codigoVerificacao);

        if (usuario == null || usuario.isVerificado()) {
            return false;
        } else {
            usuario.setVerificado(true);
            userRepository.save(usuario);
            return true;
        }
    }

    @Async
    private void enviarEmailVerificacao(UsuarioEntity user, DocenteEntity docente, String baseUrl) throws UnsupportedEncodingException, MessagingException {
            String assunto = "Confirmação de cadastro";
            String remetente = "Comissão de Extensão FT";
            String body = "<p>Prezado(a) " + docente.getNome() + ",</p>";
            body += "<p>Por favor, clique no link abaixo para confirmar seu cadastro</p>";
            String urlVerificada = baseUrl + "/auth/confirmacao?codigo=" + user.getCodigoVerificacao();
            body += "<h3><a href=\"" + urlVerificada + "\">VERIFICAR</a></h3>";
            body += "<p>Atenciosamente,<br>Comissão de Extensão FT</p>";

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(email ,remetente);
            helper.setTo(docente.getEmail());
            helper.setSubject(assunto);
            helper.setText(body, true);

            javaMailSender.send(message);
    }

    @Override
    public List<ProfilesEntity> registrarPerfilAcesso() {
        List<ProfilesEntity> profilesEntities = new ArrayList<>();

        if (profilesRepository.findAll().isEmpty()) {
            var adminProfiles = new ProfilesEntity();
            adminProfiles.setName(Profiles.ROLE_ADMIN);
            var userProfile = new ProfilesEntity();
            userProfile.setName(Profiles.ROLE_USER);

            profilesEntities.add(profilesRepository.save(adminProfiles));
            profilesEntities.add(profilesRepository.save(userProfile));
            Logger.getAnonymousLogger().info("Perfis de acesso criados com sucesso.");

        } else {
            Logger.getAnonymousLogger().info("Perfis de acesso já foram criados.");
        }

        return profilesEntities;
    }

    private DocenteEntity salvarDocente(DocenteEntity docenteEntity) {
        return docenteRepository.save(docenteEntity);
    }
}

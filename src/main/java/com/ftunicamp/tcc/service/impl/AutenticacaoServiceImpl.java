package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.request.LoginRequest;
import com.ftunicamp.tcc.controllers.request.SignUpRequest;
import com.ftunicamp.tcc.controllers.response.JwtResponse;
import com.ftunicamp.tcc.exceptions.NegocioException;
import com.ftunicamp.tcc.model.Docente;
import com.ftunicamp.tcc.model.Profiles;
import com.ftunicamp.tcc.model.ProfilesEntity;
import com.ftunicamp.tcc.model.UsuarioEntity;
import com.ftunicamp.tcc.repositories.DocenteRepository;
import com.ftunicamp.tcc.repositories.ProfilesRepository;
import com.ftunicamp.tcc.repositories.UserRepository;
import com.ftunicamp.tcc.security.jwt.JwtUtils;
import com.ftunicamp.tcc.security.services.UserDetailsImpl;
import com.ftunicamp.tcc.service.AutenticacaoService;
import com.ftunicamp.tcc.service.EmailService;
import com.ftunicamp.tcc.utils.DocenteFactory;
import com.ftunicamp.tcc.utils.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
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
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private EmailService emailService;

    @Override
    public JwtResponse autenticarUsuario(LoginRequest loginRequest) {
        var docente = docenteRepository.findByMatricula(loginRequest.getMatricula());

        if (docente == null) {
            docente = docenteRepository.findByUser_Username(loginRequest.getMatricula());
            if (docente != null) {
                var profiles = docente.getUser().getProfiles().stream()
                        .map(ProfilesEntity::getName)
                        .collect(Collectors.toSet());
                if (!profiles.contains(Profiles.ROLE_ADMIN)) {
                    throw new NegocioException("Usuário sem permissão de administrador.");
                }
            } else {
                throw new NegocioException("Matricula inválida.");
            }
        }

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(docente.getUser().getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(authentication, userDetails);

        List<String> profiles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        if (!docente.getUser().isVerificado()) {
            throw new NegocioException("Usuário não verificado.");
        }

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                profiles);
    }

    @Override
    public String registrarUsuario(SignUpRequest signUpRequest, HttpServletRequest request) {
        if (userRepository.existsByUsername(signUpRequest.getUsername()).equals(true)) {
            return "Usuário existente";
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail()).equals(true)) {
            return "Email já está em uso.";
        }

        var user = new UsuarioEntity(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        var strProfiles = signUpRequest.getProfile();
        Set<ProfilesEntity> roles = new HashSet<>();

        if (strProfiles == null) {
            var userRole = profilesRepository.findByName(Profiles.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Erro: Perfil não encontrado."));
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
                emailService.enviarEmailVerificacao(docente, baseUrl);
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

    private Docente salvarDocente(Docente docente) {
        return docenteRepository.save(docente);
    }
}

package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.request.LoginRequest;
import com.ftunicamp.tcc.controllers.request.SignUpRequest;
import com.ftunicamp.tcc.controllers.response.JwtResponse;
import com.ftunicamp.tcc.entities.Profiles;
import com.ftunicamp.tcc.entities.ProfilesEntity;
import com.ftunicamp.tcc.entities.UserEntity;
import com.ftunicamp.tcc.repositories.DocenteRepository;
import com.ftunicamp.tcc.repositories.ProfilesRepository;
import com.ftunicamp.tcc.repositories.UserRepository;
import com.ftunicamp.tcc.security.jwt.JwtUtils;
import com.ftunicamp.tcc.security.services.UserDetailsImpl;
import com.ftunicamp.tcc.service.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    @Override
    public JwtResponse autenticarUsuario(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> profiles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                profiles);
    }

    @Override
    public String registrarUsuario(SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return "Usuário existente";
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return "Email já está em uso.";
        }

        UserEntity user = new UserEntity(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strProfiles = signUpRequest.getProfile();
        Set<ProfilesEntity> roles = new HashSet<>();

        if (strProfiles == null) {
            ProfilesEntity userRole = profilesRepository.findByName(Profiles.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strProfiles.forEach(profile -> {
                if (profile.equalsIgnoreCase("admin")) {
                    ProfilesEntity adminRole = profilesRepository.findByName(Profiles.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));
                    roles.add(adminRole);
                } else {
                    ProfilesEntity userRole = profilesRepository.findByName(Profiles.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));
                    roles.add(userRole);
                }
            });
        }

        user.setProfiles(roles);
        salvarDocente(userRepository.save(user));

        return "Usuário registrado com sucesso!";
    }

    private void salvarDocente(UserEntity usuario) {
        //implementar
    }
}

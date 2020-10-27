package com.ftunicamp.tcc.controllers;

import com.ftunicamp.tcc.controllers.request.LoginRequest;
import com.ftunicamp.tcc.controllers.request.ProfileRequest;
import com.ftunicamp.tcc.controllers.request.SignUpRequest;
import com.ftunicamp.tcc.controllers.response.JwtResponse;
import com.ftunicamp.tcc.entities.ProfilesEntity;
import com.ftunicamp.tcc.service.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private AutenticacaoService autenticacaoService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> autenticarUsuario(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(autenticacaoService.autenticarUsuario(loginRequest));
    }

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarUsuario(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(autenticacaoService.registrarUsuario(signUpRequest));
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<ProfilesEntity>> registrarPerfilAcesso() {
        return ResponseEntity.ok(autenticacaoService.registrarPerfilAcesso());
    }
}

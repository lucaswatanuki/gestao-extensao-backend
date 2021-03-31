package com.ftunicamp.tcc.controllers;

import com.ftunicamp.tcc.controllers.request.LoginRequest;
import com.ftunicamp.tcc.controllers.request.SignUpRequest;
import com.ftunicamp.tcc.controllers.response.JwtResponse;
import com.ftunicamp.tcc.model.ProfilesEntity;
import com.ftunicamp.tcc.service.AutenticacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
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
    public ResponseEntity<String> registrarUsuario(@RequestBody SignUpRequest signUpRequest,
                                                   HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        return ResponseEntity.ok(autenticacaoService.registrarUsuario(signUpRequest, request));
    }

    @GetMapping("/profiles")
    public ResponseEntity<List<ProfilesEntity>> registrarPerfilAcesso() {
        return ResponseEntity.ok(autenticacaoService.registrarPerfilAcesso());
    }

    @GetMapping("/confirmacao")
    public ResponseEntity<String> confirmarCadastro(@RequestParam("codigo") String codigo) {
        var verificado = autenticacaoService.verificarUsuario(codigo);
        if (verificado) {
            return ResponseEntity.ok("Conta confirmada com sucesso!");
        }
        return ResponseEntity.badRequest().body("Erro ao verificar conta");
    }
}

package com.ftunicamp.tcc.controllers;

import com.ftunicamp.tcc.dto.PasswordDto;
import com.ftunicamp.tcc.dto.PasswordTokenDto;
import com.ftunicamp.tcc.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/senha")
public class PasswordController {

    @Value("${frontend.url}")
    String frontUrl;

    @Autowired
    private PasswordService passwordService;

    @PostMapping("/reset")
    public ResponseEntity<Void> resetarSenha(@RequestBody PasswordDto dto, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        passwordService.resetarSenha(dto, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verificarToken/{token}")
    public ResponseEntity<PasswordTokenDto> verificarToken(@PathVariable("token") String token) {
        return ResponseEntity.ok(PasswordTokenDto.builder()
                .valido(passwordService.validarToken(token))
                .build());
    }

    @PostMapping("/alterarSenha")
    public ResponseEntity<Void> salvarSenha(@RequestBody PasswordDto dto) {
        var tokenValido = passwordService.validarToken(dto.getToken());
        if (!tokenValido) {
            return ResponseEntity.badRequest().build();
        }
        passwordService.alterarSenha(dto);
        return ResponseEntity.ok().build();
    }
}

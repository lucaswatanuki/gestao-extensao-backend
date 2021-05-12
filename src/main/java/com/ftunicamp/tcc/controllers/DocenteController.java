package com.ftunicamp.tcc.controllers;

import com.ftunicamp.tcc.controllers.response.DocenteResponse;
import com.ftunicamp.tcc.dto.AlocacaoDto;
import com.ftunicamp.tcc.dto.PasswordDto;
import com.ftunicamp.tcc.dto.UsuarioDto;
import com.ftunicamp.tcc.service.DocenteService;
import com.ftunicamp.tcc.service.PasswordService;
import com.ftunicamp.tcc.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/docente")
public class DocenteController {

    @Autowired
    private DocenteService docenteService;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/todos")
    public ResponseEntity<List<DocenteResponse>> listarDocentes() {
        return ResponseEntity.ok(docenteService.listarDocentes());
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deletarDocente(@PathVariable("username") String username) {
        docenteService.deletarDocente(username);
        return ResponseEntity.ok("Usuário deletado.");
    }

    @GetMapping("/{id}/alocacoes")
    public ResponseEntity<List<AlocacaoDto>> getAlocacoesDocente(@PathVariable("id") long docenteId) {
        return ResponseEntity.ok(docenteService.consultarAlocacoesDocente(docenteId));
    }

    @GetMapping("/alocacoes")
    public ResponseEntity<List<AlocacaoDto>> getAlocacoes() {
        return ResponseEntity.ok(docenteService.getAlocacoes());
    }

    @PutMapping("/alocacoes")
    public ResponseEntity<Void> atualizarAlocacao(@RequestBody AlocacaoDto alocacaoDto) {
        docenteService.atualizarAlocacao(alocacaoDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> alterarDadosUsuario(@PathVariable("id") long usuarioId,
                                                    @RequestBody UsuarioDto request) {
        try {
            usuarioService.alterarDadosUsuario(usuarioId, request);
        } catch (Exception e) {
            Logger.getAnonymousLogger().log(Level.WARNING, e.getMessage());
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> getDadosUsuario(@PathVariable("id") long id) {
        return ResponseEntity.ok(usuarioService.getDadosUsuario(id));
    }

    @PutMapping("/alterarSenha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> alterarSenha(@RequestBody PasswordDto request) {
        passwordService.alterarSenha(request);
        return ResponseEntity.noContent().build();
    }

}

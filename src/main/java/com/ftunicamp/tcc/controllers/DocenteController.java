package com.ftunicamp.tcc.controllers;

import com.ftunicamp.tcc.controllers.request.RelatorioRequest;
import com.ftunicamp.tcc.controllers.response.DocenteResponse;
import com.ftunicamp.tcc.controllers.response.RelatorioResponse;
import com.ftunicamp.tcc.dto.AlocacaoDto;
import com.ftunicamp.tcc.dto.UsuarioDto;
import com.ftunicamp.tcc.service.DocenteService;
import com.ftunicamp.tcc.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/docente")
public class DocenteController {

    @Autowired
    DocenteService docenteService;

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
    public ResponseEntity<List<AlocacaoDto>> getAlocacoes(@PathVariable("id") long docenteId) {
        return ResponseEntity.ok(docenteService.consultarAlocacoes(docenteId));
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<UsuarioDto> alterarDadosUsuario(@RequestBody RelatorioRequest request) {
//
//        return ResponseEntity.ok(usuarioService.getDadosUsuario(request));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDto> getDadosUsuario(@PathVariable("id") long id) {
        return ResponseEntity.ok(usuarioService.getDadosUsuario(id));
    }

}

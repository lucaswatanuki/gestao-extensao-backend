package com.ftunicamp.tcc.controllers;

import com.ftunicamp.tcc.controllers.response.DocenteResponse;
import com.ftunicamp.tcc.service.DocenteService;
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

    @GetMapping("/todos")
    public ResponseEntity<List<DocenteResponse>> listarDocentes() {
        return ResponseEntity.ok(docenteService.listarDocentes());
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<String> deletarDocente(@PathVariable("username") String username) {
         docenteService.deletarDocente(username);
         return ResponseEntity.ok("Usuário deletado.");
    }

}

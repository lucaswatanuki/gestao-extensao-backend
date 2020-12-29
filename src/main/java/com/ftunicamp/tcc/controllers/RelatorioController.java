package com.ftunicamp.tcc.controllers;

import com.ftunicamp.tcc.controllers.response.RelatorioResponse;
import com.ftunicamp.tcc.entities.StatusAtividade;
import com.ftunicamp.tcc.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/report")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/{id}")
    public ResponseEntity<List<RelatorioResponse>> getRelatorio(@PathVariable("id") long id, @RequestHeader StatusAtividade status,
                                                                @RequestParam String dataInicio,
                                                                @RequestParam String dataFim) throws ParseException {

        return ResponseEntity.ok(relatorioService.gerarRelatorioPorDocente(id, status, dataInicio, dataFim));
    }
}

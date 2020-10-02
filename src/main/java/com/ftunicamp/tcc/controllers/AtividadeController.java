package com.ftunicamp.tcc.controllers;


import com.ftunicamp.tcc.controllers.request.AtividadeRequest;
import com.ftunicamp.tcc.controllers.response.AtividadeResponse;
import com.ftunicamp.tcc.controllers.response.AutorizacaoResponse;
import com.ftunicamp.tcc.service.AtividadeService;
import com.ftunicamp.tcc.service.AutorizacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/atividade")
public class AtividadeController {

    @Autowired
    private AtividadeService atividadeService;

    @Autowired
    private AutorizacaoService autorizacaoService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/incluir")
    public ResponseEntity<AtividadeResponse> incluirAtividade(@RequestHeader("jwt") String jwt,
                                                              @RequestBody AtividadeRequest request) {
        return ResponseEntity.ok(atividadeService.cadastrarAtividade(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtividadeResponse> buscarAtividade(@RequestHeader("jwt") String jwt,
                                                             @PathVariable("id") Long id) {
        return ResponseEntity.ok(atividadeService.buscarAtividade(id));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void excluirAtividade(@RequestHeader("jwt") String jwt,
                                 @PathVariable("id") Long id) {
        atividadeService.excluirAtividade(id);
    }

    @PostMapping(value = "/autorizar/{id}")
    public ResponseEntity<AutorizacaoResponse> autorizarAtividade(@RequestHeader("jwt") String jwt,
                                                                  @RequestHeader("id") Long id) {
        return ResponseEntity.ok(autorizacaoService.incluirAutorizacao(id));
    }

    @GetMapping("/autorizacao/{id}")
    public ResponseEntity<AutorizacaoResponse> buscarAutorizacao(@RequestHeader("jwt") String jwt,
                                                                 @RequestHeader("id") Long id) {
        return ResponseEntity.ok(autorizacaoService.buscarAutorizacao(id));
    }
}

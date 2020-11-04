package com.ftunicamp.tcc.controllers;


import com.ftunicamp.tcc.controllers.request.ConvenioRequest;
import com.ftunicamp.tcc.controllers.request.CursoExtensaoRequest;
import com.ftunicamp.tcc.controllers.request.RegenciaRequest;
import com.ftunicamp.tcc.controllers.request.UnivespRequest;
import com.ftunicamp.tcc.controllers.response.AtividadeResponse;
import com.ftunicamp.tcc.controllers.response.AutorizacaoResponse;
import com.ftunicamp.tcc.controllers.response.Response;
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
    @PostMapping(value = "/convenio")
    public ResponseEntity<Response<String>> incluirAtividadeConvenio(@RequestBody ConvenioRequest request) {
        return ResponseEntity.ok(atividadeService.cadastrarConvenio(request));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/curso")
    public ResponseEntity<Response<String>> incluirCursoExtensao(@RequestBody CursoExtensaoRequest request) {
        return ResponseEntity.ok(atividadeService.cadastrarCursoExtensao(request));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/regencia")
    public ResponseEntity<Response<String>> incluirRegenciaConcomitante(@RequestBody RegenciaRequest request) {
        return ResponseEntity.ok(atividadeService.cadastrarRegencia(request));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/univesp")
    public ResponseEntity<Response<String>> incluirAtividadeUnivesp(@RequestBody UnivespRequest request) {
        return ResponseEntity.ok(atividadeService.cadastrarAtividadeUnivesp(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtividadeResponse> buscarAtividade(@PathVariable("id") Long id) {
        return ResponseEntity.ok(atividadeService.buscarAtividade(id));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void excluirAtividade(@PathVariable("id") Long id) {
        atividadeService.excluirAtividade(id);
    }

    @PostMapping(value = "/autorizar/{id}")
    public ResponseEntity<AutorizacaoResponse> autorizarAtividade(@RequestHeader("id") Long id) {
        return ResponseEntity.ok(autorizacaoService.incluirAutorizacao(id));
    }

    @GetMapping("/autorizacao/{id}")
    public ResponseEntity<AutorizacaoResponse> buscarAutorizacao(@RequestHeader("id") Long id) {
        return ResponseEntity.ok(autorizacaoService.buscarAutorizacao(id));
    }
}

package com.ftunicamp.tcc.controllers;


import com.ftunicamp.tcc.controllers.request.*;
import com.ftunicamp.tcc.controllers.response.*;
import com.ftunicamp.tcc.model.TipoAtividade;
import com.ftunicamp.tcc.service.AtividadeService;
import com.ftunicamp.tcc.service.AutorizacaoService;
import com.ftunicamp.tcc.service.PdfService;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/atividade")
public class AtividadeController {

    @Autowired
    private AtividadeService atividadeService;

    @Autowired
    private AutorizacaoService autorizacaoService;

    @Autowired
    private PdfService pdfService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/convenio")
    public ResponseEntity<AtividadeResponse> incluirAtividadeConvenio(@RequestBody ConvenioRequest request) throws UnsupportedEncodingException, MessagingException {
        return ResponseEntity.ok(atividadeService.cadastrarConvenio(request));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/curso")
    public ResponseEntity<AtividadeResponse> incluirCursoExtensao(@RequestBody CursoExtensaoRequest request) {
        return ResponseEntity.ok(atividadeService.cadastrarCursoExtensao(request));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/regencia")
    public ResponseEntity<AtividadeResponse> incluirRegenciaConcomitante(@RequestBody RegenciaRequest request) {
        return ResponseEntity.ok(atividadeService.cadastrarRegencia(request));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/univesp")
    public ResponseEntity<Response<String>> incluirAtividadeUnivesp(@RequestBody UnivespRequest request) {
        return ResponseEntity.ok(atividadeService.cadastrarAtividadeUnivesp(request));
    }


    @GetMapping("/listar")
    public ResponseEntity<List<AtividadeResponse>> listarAtividades() {
        return ResponseEntity.ok(atividadeService.listarAtividades());
    }

    @PostMapping(value = "/autorizar/{id}")
    public ResponseEntity<Void> autorizarAtividade(@PathVariable("id") Long id,
                                                   @RequestBody AutorizacaoRequest request) {
        autorizacaoService.incluirAutorizacao(id, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/autorizacao/{id}")
    public ResponseEntity<AutorizacaoResponse> buscarAutorizacao(@PathVariable("id") Long id) {
        return ResponseEntity.ok(autorizacaoService.buscarAutorizacao(id));
    }

    @GetMapping("/autorizacao")
    public ResponseEntity<List<AutorizacaoResponse>> getAutorizacoes() {
        return ResponseEntity.ok(autorizacaoService.listarAutorizacoes());
    }

    @GetMapping("/download/{tipoAtividade}/{id}")
    public void downloadPDFResource(HttpServletResponse response,
                                    @PathVariable("tipoAtividade") TipoAtividade tipoAtividade,
                                    @PathVariable("id") Long id) {
        try {
            Path file = Paths.get(pdfService.generatePdf(tipoAtividade, id).getAbsolutePath());
            if (Files.exists(file)) {
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition",
                        "attachment; filename=" + file.getFileName());
                Files.copy(file, response.getOutputStream());
                response.getOutputStream().flush();
            }
        } catch (DocumentException | IOException ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/curso-extensao/{id}")
    public ResponseEntity<CursoExtensaoDto> getCursoExtensao(@PathVariable("id") long id) {
        return ResponseEntity.ok(atividadeService.consultarCursoExtensao(id));
    }

    @GetMapping("/convenio/{id}")
    public ResponseEntity<ConvenioDto> getConvenio(@PathVariable("id") long id) {
        var response = atividadeService.consultarConvenio(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/regencia/{id}")
    public ResponseEntity<RegenciaDto> getRegencia(@PathVariable("id") long id) {
        return ResponseEntity.ok(atividadeService.consultarRegencia(id));
    }

    @PutMapping("/regencia")
    public ResponseEntity<Void> updateRegencia(@RequestBody RegenciaDto regenciaDto) {
        atividadeService.updateRegencia(regenciaDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/curso-extensao")
    public ResponseEntity<Void> updateCursoExtensao(@RequestBody CursoExtensaoDto cursoExtensaoDto) {
        atividadeService.updateCursoExtensao(cursoExtensaoDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/convenio")
    public ResponseEntity<Void> updateConvenio(@RequestBody ConvenioDto convenioDto) {
        atividadeService.updateConvenio(convenioDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAtividade(@PathVariable("id") long id) {
        atividadeService.excluirAtividade(id);
        return ResponseEntity.ok().build();
    }
}

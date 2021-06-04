package com.ftunicamp.tcc.controllers;

import com.ftunicamp.tcc.controllers.response.Response;
import com.ftunicamp.tcc.dto.ArquivoDto;
import com.ftunicamp.tcc.service.ArquivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/arquivos")
public class ArquivoController {

    @Autowired
    private ArquivoService arquivoService;

    @PostMapping("/upload/{atividadeId}")
    public ResponseEntity<Response<String>> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("atividadeId") long atividadeId) {
        var mensagem = "";
        try {
            arquivoService.salvar(file, atividadeId);

            mensagem = "Upload feito com sucesso: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(Response.<String>builder()
                            .mensagem(mensagem)
                            .build());
        } catch (Exception e) {
            mensagem = "Falha ao fazer upload do arquivo: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(Response.<String>builder()
                            .mensagem(mensagem)
                            .build());
        }
    }

    @GetMapping(value = "/download/{id}")
    public ResponseEntity<byte[]> getArquivo(@PathVariable long id) {
        var arquivo = arquivoService.getArquivo(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + arquivo.getNome() + "\"")
                .contentType(MediaType.parseMediaType(arquivo.getTipo()))
                .body(arquivo.getData());
    }

    @GetMapping("/{atividadeId}")
    public ResponseEntity<List<ArquivoDto>> getArquivos(@PathVariable("atividadeId") long atividadeId) {
        var arquivos = arquivoService.getArquivos(atividadeId);

        return ResponseEntity.ok()
                .body(arquivos);
    }
}

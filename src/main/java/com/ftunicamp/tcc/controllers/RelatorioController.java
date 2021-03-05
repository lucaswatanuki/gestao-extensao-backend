package com.ftunicamp.tcc.controllers;

import com.ftunicamp.tcc.controllers.request.RelatorioRequest;
import com.ftunicamp.tcc.controllers.response.RelatorioResponse;
import com.ftunicamp.tcc.service.PDFExportService;
import com.ftunicamp.tcc.service.RelatorioService;
import com.ftunicamp.tcc.service.impl.RelatorioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/report")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

     @PostMapping("/docente")
    public ResponseEntity<List<RelatorioResponse>> getRelatorio(@RequestBody RelatorioRequest request) throws ParseException {

        return ResponseEntity.ok(relatorioService.gerarRelatorioPorDocente(request));
    }

    @PostMapping("/export")
    public void exportToPdf(HttpServletResponse response, @RequestBody RelatorioRequest request) throws IOException, ParseException {
        var dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        var currentTime = dateFormat.format(new Date());
        response.setContentType("application/pdf");
        var headerKey = "Content-Disposition";
        var headervalue = String.format("attachment; filename=relatorio_%s.pdf", currentTime);

        response.setHeader(headerKey, headervalue);

        var exporter = new RelatorioServiceImpl(relatorioService.gerarRelatorioPorDocente(request));

        exporter.export(response);

    }
}

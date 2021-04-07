package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.utils.TipoPDF;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;

public interface PdfService {
    File generatePdf(TipoPDF tipo, long idAtividade) throws IOException;

    File renderPdf(String html) throws IOException;
}

package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.model.TipoAtividade;

import java.io.File;
import java.io.IOException;

public interface PdfService {
    File generatePdf(TipoAtividade tipo, long idAtividade) throws IOException;

    File renderPdf(String html) throws IOException;
}

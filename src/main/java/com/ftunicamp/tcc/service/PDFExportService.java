package com.ftunicamp.tcc.service;

import com.lowagie.text.pdf.PdfPTable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PDFExportService {
    void writeTableHeader(PdfPTable table);
    void writeTableData(PdfPTable table);
    void export(HttpServletResponse response) throws IOException;
}

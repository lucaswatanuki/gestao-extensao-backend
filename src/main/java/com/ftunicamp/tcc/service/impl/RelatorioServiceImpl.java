package com.ftunicamp.tcc.service.impl;

import com.ftunicamp.tcc.controllers.request.RelatorioRequest;
import com.ftunicamp.tcc.controllers.response.RelatorioResponse;
import com.ftunicamp.tcc.entities.Atividade;
import com.ftunicamp.tcc.entities.StatusAtividade;
import com.ftunicamp.tcc.repositories.AtividadeRepository;
import com.ftunicamp.tcc.service.PDFExportService;
import com.ftunicamp.tcc.service.RelatorioService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service("relatorio")
public class RelatorioServiceImpl implements RelatorioService, PDFExportService {

    @Autowired
    private AtividadeRepository atividadeRepository;

    private List<RelatorioResponse> dadosRelatorio;

    public RelatorioServiceImpl(List<RelatorioResponse> dadosRelatorio) {
        this.dadosRelatorio = dadosRelatorio;
    }

    @Override
    public List<RelatorioResponse> gerarRelatorioPorDocente(RelatorioRequest request) throws ParseException {

        var inicio = request.getDataInicio();
        var fim = request.getDataFim();
        var statusAtividade = request.getStatusAtividade();
        var idDocente = request.getIdDocente();

        final List<Atividade> atividades;

        if (statusAtividade.equals(StatusAtividade.TODOS)) {
            atividades = atividadeRepository.gerarRelatorioTodasAtividadesPorDocente(idDocente, inicio, fim);
        } else {
            atividades = atividadeRepository.gerarRelatorioAtividadesPorStatusEDocente(idDocente, statusAtividade, inicio, fim);
        }

        final List<RelatorioResponse> response = new ArrayList<>();

        atividades.forEach(atividade -> response.add(mapToResponse(atividade)));

        return response;
    }

    private RelatorioResponse mapToResponse(Atividade atividade) {
        return RelatorioResponse.builder()
                .nomeDocente(atividade.getDocente().getNome())
                .statusAtividade(atividade.getStatus())
                .tipoAtividade(atividade.getTipoAtividade())
                .dataInicio(atividade.getDataInicio().toLocalDate())
                .dataFim(atividade.getDataFim().toLocalDate())
                .prazo(atividade.getPrazo())
                .build();
    }

    @Override
    public List<RelatorioResponse> gerarRelatorioGeral(RelatorioRequest request) {

        var inicio = request.getDataInicio();
        var fim = request.getDataFim();
        var statusAtividade = request.getStatusAtividade();

        final List<Atividade> atividades;

        if (statusAtividade.equals(StatusAtividade.TODOS)) {
            atividades = atividadeRepository.gerarRelatorioTodasAtividades(inicio, fim);
        } else {
            atividades = atividadeRepository.gerarRelatorioTodasAtividadesPorStatus(inicio, fim, statusAtividade);
        }

        final List<RelatorioResponse> response = new ArrayList<>();

        atividades.forEach(atividade -> response.add(mapToResponse(atividade)));

        return response;
    }

    @Override
    public void writeTableHeader(PdfPTable table) {
        var cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLACK);
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        var font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Nome Docente", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Tipo Atividade", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Data Inicio", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Data Fim", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Prazo (meses)", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Status", font));
        table.addCell(cell);
    }

    @Override
    public void writeTableData(PdfPTable table) {
        dadosRelatorio.forEach(item -> {
            table.addCell(item.getNomeDocente());
            table.addCell(item.getTipoAtividade());
            table.addCell(item.getDataInicio().toString());
            table.addCell(item.getDataFim().toString());
            table.addCell(String.valueOf(item.getPrazo()));
            table.addCell(item.getStatusAtividade().getStatus());
        });

    }

    @Override
    public void export(HttpServletResponse response) throws IOException, DocumentException {
        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        var font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.RED);
        font.setSize(18);
        Paragraph titulo = new Paragraph("Relatório de atividades", font);
        titulo.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(titulo);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setSpacingBefore(15);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);
        document.close();
    }
}

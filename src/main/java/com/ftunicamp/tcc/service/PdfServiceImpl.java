package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.model.ConvenioEntity;
import com.ftunicamp.tcc.model.CursoExtensaoEntity;
import com.ftunicamp.tcc.repositories.AtividadeRepository;
import com.ftunicamp.tcc.repositories.ConvenioRepository;
import com.ftunicamp.tcc.repositories.CursoRepository;
import com.ftunicamp.tcc.utils.TipoPDF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

@Service
public class PdfServiceImpl implements PdfService {

    private static final String PDF_RESOURCES = "/pdf-resources/";
    private final SpringTemplateEngine templateEngine;
    private final AtividadeRepository atividadeRepository;
    private final ConvenioRepository convenioRepository;
    private final CursoRepository cursoRepository;

    @Autowired
    public PdfServiceImpl(SpringTemplateEngine templateEngine, AtividadeRepository atividadeRepository,
                          ConvenioRepository convenioRepository, CursoRepository cursoRepository) {
        this.templateEngine = templateEngine;
        this.atividadeRepository = atividadeRepository;
        this.convenioRepository = convenioRepository;
        this.cursoRepository = cursoRepository;
    }

    @Override
    public File generatePdf(TipoPDF tipo, long idAtividade) throws IOException {
        Context context = getContext(tipo, idAtividade);
        String html = loadAndFillTemplate(context, tipo.getValue());
        return renderPdf(html);
    }

    @Override
    public File renderPdf(String html) throws IOException {
        File file = File.createTempFile("atividade", ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
        renderer.setDocumentFromString(html, new ClassPathResource(PDF_RESOURCES).getURL().toExternalForm());
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();
        file.deleteOnExit();
        return file;
    }

    private Context getContext(TipoPDF tipoPDF, long id) {
        Context context = new Context();
        switch (tipoPDF) {
            case CURSO:
                Optional<CursoExtensaoEntity> curso = cursoRepository.findById(id);
                curso.ifPresent(cursoExtensaoEntity -> context.setVariable("curso", cursoExtensaoEntity));
                break;
            case CONVENIO:
                Optional<ConvenioEntity> convenio = convenioRepository.findById(id);
                convenio.ifPresent(convenioEntity -> context.setVariable("convenio", convenioEntity));
                break;
        }
        return context;
    }

    private String loadAndFillTemplate(Context context, String tipo) {
        return templateEngine.process(String.format("template_%s", tipo), context);
    }
}

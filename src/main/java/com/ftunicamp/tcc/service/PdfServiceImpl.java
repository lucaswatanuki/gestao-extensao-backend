package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.model.ConvenioEntity;
import com.ftunicamp.tcc.model.CursoExtensaoEntity;
import com.ftunicamp.tcc.model.RegenciaEntity;
import com.ftunicamp.tcc.model.TipoAtividade;
import com.ftunicamp.tcc.repositories.ConvenioRepository;
import com.ftunicamp.tcc.repositories.CursoRepository;
import com.ftunicamp.tcc.repositories.RegenciaRepository;
import com.ftunicamp.tcc.utils.ContextPdf;
import com.ftunicamp.tcc.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

import static com.ftunicamp.tcc.utils.DateUtils.nomeDoMes;

@Service
public class PdfServiceImpl implements PdfService {

    private static final String PDF_RESOURCES = "/pdf-resources/";
    @Value("${coordenador.nome}")
    private String nomeCoordenador;
    private final SpringTemplateEngine templateEngine;
    private final ConvenioRepository convenioRepository;
    private final CursoRepository cursoRepository;
    private final RegenciaRepository regenciaRepository;

    @Autowired
    public PdfServiceImpl(SpringTemplateEngine templateEngine,
                          ConvenioRepository convenioRepository,
                          CursoRepository cursoRepository,
                          RegenciaRepository regenciaRepository) {
        this.templateEngine = templateEngine;
        this.convenioRepository = convenioRepository;
        this.cursoRepository = cursoRepository;
        this.regenciaRepository = regenciaRepository;
    }

    @Override
    public File generatePdf(TipoAtividade tipo, long idAtividade) throws IOException {
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

    private Context getContext(TipoAtividade tipoPDF, long id) {
        var context = new Context();
        switch (tipoPDF) {
            case CURSO:
                Optional<CursoExtensaoEntity> curso = cursoRepository.findById(id);
                curso.ifPresent(cursoExtensaoEntity -> {
                    context.setVariable("curso", cursoExtensaoEntity);
                    context.setVariable("coordenador", nomeCoordenador);
                    context.setVariable("dia", cursoExtensaoEntity.getDataModificacao().getDayOfMonth());
                    context.setVariable("mes", nomeDoMes(cursoExtensaoEntity.getDataModificacao().getMonthValue()));
                    context.setVariable("ano", cursoExtensaoEntity.getDataModificacao().getYear());
                } );
                break;
            case CONVENIO:
                Optional<ConvenioEntity> convenio = convenioRepository.findById(id);
                convenio.ifPresent(convenioEntity -> {
                    context.setVariable("convenio", convenioEntity);
                    context.setVariable("coordenador", nomeCoordenador);
                    context.setVariable("dia", convenioEntity.getDataModificacao().getDayOfMonth());
                    context.setVariable("mes", nomeDoMes(convenioEntity.getDataModificacao().getMonthValue()));
                    context.setVariable("ano", convenioEntity.getDataModificacao().getYear());
                } );
                break;
            case REGENCIA:
                Optional<RegenciaEntity> regencia = regenciaRepository.findById(id);
                regencia.ifPresent(regenciaEntity -> context.setVariable("regencia", regenciaEntity));
        }
        return context;
    }

    private String loadAndFillTemplate(Context context, String tipo) {
        return templateEngine.process(String.format("template_%s", tipo), context);
    }
}

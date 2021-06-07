package com.ftunicamp.tcc.service;

import com.ftunicamp.tcc.model.*;
import com.ftunicamp.tcc.repositories.ConvenioRepository;
import com.ftunicamp.tcc.repositories.CursoRepository;
import com.ftunicamp.tcc.repositories.RegenciaRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ftunicamp.tcc.utils.DateUtils.nomeDoMes;

@Service
public class PdfServiceImpl implements PdfService {

    private static final String PDF_RESOURCES = "/pdf-resources/";
    private static final String ALOCACOES = "alocacoes";
    private static final String COORDENADOR = "coordenador";
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
        var context = getContext(tipo, idAtividade);
        String html = loadAndFillTemplate(context, tipo.getValue());
        return renderPdf(html);
    }

    @Override
    public File renderPdf(String html) throws IOException {
        var file = File.createTempFile("atividade", ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        var renderer = new ITextRenderer(20f * 4f / 3f, 20);
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
                Optional<CursoExtensao> curso = cursoRepository.findById(id);
                curso.ifPresent(cursoExtensao -> {
                    context.setVariable("curso", cursoExtensao);
                    context.setVariable(COORDENADOR, nomeCoordenador);
                    context.setVariable("dia", cursoExtensao.getDataModificacao().getDayOfMonth());
                    context.setVariable("mes", nomeDoMes(cursoExtensao.getDataModificacao().getMonthValue()));
                    context.setVariable("ano", cursoExtensao.getDataModificacao().getYear());
                    context.setVariable(ALOCACOES, getAlocacoesAprovadas(cursoExtensao));
                });
                break;
            case CONVENIO:
                Optional<Convenio> convenio = convenioRepository.findById(id);
                convenio.ifPresent(convenioEntity -> {
                    context.setVariable("convenio", convenioEntity);
                    context.setVariable(COORDENADOR, nomeCoordenador);
                    context.setVariable("dia", convenioEntity.getDataModificacao().getDayOfMonth());
                    context.setVariable("mes", nomeDoMes(convenioEntity.getDataModificacao().getMonthValue()));
                    context.setVariable("ano", convenioEntity.getDataModificacao().getYear());
                    context.setVariable(ALOCACOES, getAlocacoesAprovadas(convenioEntity));
                });
                break;
            case REGENCIA:
                Optional<Regencia> regencia = regenciaRepository.findById(id);
                regencia.ifPresent(regenciaEntity -> {
                    context.setVariable("regencia", regenciaEntity);
                    context.setVariable(COORDENADOR, nomeCoordenador);
                    context.setVariable("dia", regenciaEntity.getDataModificacao().getDayOfMonth());
                    context.setVariable("mes", nomeDoMes(regenciaEntity.getDataModificacao().getMonthValue()));
                    context.setVariable("ano", regenciaEntity.getDataModificacao().getYear());
                    context.setVariable(ALOCACOES, getAlocacoesAprovadas(regenciaEntity));
                });
        }
        return context;
    }

    private <T extends Atividade> List<Alocacao> getAlocacoesAprovadas(T atividade) {
        return atividade.getDocente().getAlocacao().stream()
                .filter(alocacao -> alocacao.getAtividade().getStatus().equals(StatusAtividade.EM_ANDAMENTO) ||
                        alocacao.getAtividade().getStatus().equals(StatusAtividade.CONCLUIDA))
                .filter(alocacao -> (alocacao.getAno() >= alocacao.getAno() - 1) && (alocacao.getAno() <= alocacao.getAno() + 1))
                .collect(Collectors.toList());
    }

    private String loadAndFillTemplate(Context context, String tipo) {
        return templateEngine.process(String.format("template_%s", tipo), context);
    }
}

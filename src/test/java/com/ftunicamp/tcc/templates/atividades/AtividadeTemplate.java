package com.ftunicamp.tcc.templates.atividades;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.ftunicamp.tcc.model.Convenio;
import com.ftunicamp.tcc.model.StatusAtividade;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AtividadeTemplate implements TemplateLoader {

    public static final String CONVENIO = "convenio";

    @Override
    public void load() {
        Fixture.of(Convenio.class).addTemplate(CONVENIO, new Rule(){
            {
                add("id", 1L);
                add("dataCriacao", LocalDate.now());
                add("prazo", 1L);
                add("projeto", "Projeto convenio teste");
                add("dataInicio", LocalDateTime.now());
                add("dataFim", LocalDateTime.now());
                add("status", StatusAtividade.EM_ANDAMENTO);
            }
        });
    }
}

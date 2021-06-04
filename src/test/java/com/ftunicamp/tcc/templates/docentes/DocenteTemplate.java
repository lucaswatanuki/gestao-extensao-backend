package com.ftunicamp.tcc.templates.docentes;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import com.ftunicamp.tcc.model.Docente;

public class DocenteTemplate implements TemplateLoader {

    public static final String DOCENTE = "docente";

    @Override
    public void load() {
        Fixture.of(Docente.class).addTemplate(DOCENTE, new Rule(){
            {
                add("id", 1L);
                add("nome", "Lucas Watanuki");
                add("cpf", "46901514824");
                add("matricula", "202143");
            }
        });
    }
}

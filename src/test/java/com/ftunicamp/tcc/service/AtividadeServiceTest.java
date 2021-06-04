package com.ftunicamp.tcc.service;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import com.ftunicamp.tcc.dto.Sessao;
import com.ftunicamp.tcc.model.Atividade;
import com.ftunicamp.tcc.model.ConvenioEntity;
import com.ftunicamp.tcc.model.Docente;
import com.ftunicamp.tcc.repositories.AtividadeRepository;
import com.ftunicamp.tcc.repositories.DocenteRepository;
import com.ftunicamp.tcc.security.jwt.JwtUtils;
import com.ftunicamp.tcc.service.impl.AtividadeServiceImpl;
import com.ftunicamp.tcc.templates.atividades.AtividadeTemplate;
import com.ftunicamp.tcc.templates.docentes.DocenteTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static br.com.six2six.fixturefactory.Fixture.from;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AtividadeServiceTest {

    @Mock
    private AtividadeRepository<Atividade> atividadeRepository;

    @Mock
    private DocenteRepository docenteRepository;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AtividadeServiceImpl atividadeService;

    private Sessao sessao;

    @Before
    public void setUp(){
        FixtureFactoryLoader.loadTemplates("com.ftunicamp.tcc.templates");
        this.sessao = new Sessao();
        sessao.setUsername("teste");
    }

    @Test
    public void deveListarAtividadesPorDocente() {
        ConvenioEntity atividadeModel = from(ConvenioEntity.class).gimme(AtividadeTemplate.CONVENIO);
        Docente docenteModel = from(Docente.class).gimme(DocenteTemplate.DOCENTE);

        when(jwtUtils.getSessao()).thenReturn(sessao);
        when(docenteRepository.findByUser_Username(anyString())).thenReturn(docenteModel);
        when(atividadeRepository.findAllByDocente(docenteModel)).thenReturn(singletonList(atividadeModel));

        var atividades = atividadeService.listarAtividades();
        var atividadeResponse = atividades.get(0);

        assertThat(atividades).isNotNull();
        assertThat(atividadeResponse.getId()).isEqualTo(atividadeModel.getId());
    }
}

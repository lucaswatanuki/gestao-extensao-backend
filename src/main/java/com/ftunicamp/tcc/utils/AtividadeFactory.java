package com.ftunicamp.tcc.utils;

import com.ftunicamp.tcc.controllers.request.ConvenioRequest;
import com.ftunicamp.tcc.controllers.request.CursoExtensaoRequest;
import com.ftunicamp.tcc.controllers.request.RegenciaRequest;
import com.ftunicamp.tcc.controllers.request.UnivespRequest;
import com.ftunicamp.tcc.entities.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AtividadeFactory {

    private AtividadeFactory() {
    }

    public static Atividade criarConvenio(ConvenioRequest request, DocenteEntity docente) {
        var convenio = new ConvenioEntity();
        convenio.setDocente(docente);

        convenio.setHoraMensal(request.getHoraMensal());
        convenio.setHoraSemanal(request.getHoraSemanal());
        convenio.setValorBruto(request.getValorBruto());
        convenio.setPrazo(request.getPrazo());
        convenio.setCoordenador(request.getCoordenador());
        convenio.setDescricao(request.getDescricao());
        convenio.setInstituicao(request.getInstituicao());
        convenio.setProjeto(request.getProjeto());

        convenio.setDataCriacao(LocalDate.now());
        convenio.setDataModificacao(LocalDate.now());
        //Mapear request para entidade - mapper struct
        return convenio;
    }

    public static CursoExtensaoEntity criarCurso(CursoExtensaoRequest request, DocenteEntity docente) {
        var curso = new CursoExtensaoEntity();
        curso.setDocente(docente);

        //Mapear request para entidade - mapper struct
        return curso;
    }

    public static RegenciaEntity criarRegencia(RegenciaRequest request, DocenteEntity docente) {
        var regencia = new RegenciaEntity();
        regencia.setDocente(docente);

        //Mapear request para entidade - mapper struct
        return regencia;
    }

    public static UnivespEntity criarAtividadeUnivesp(UnivespRequest request, DocenteEntity docente) {
        var atividadeUnivesp = new UnivespEntity();
        atividadeUnivesp.setDocente(docente);

        //Mapear request para entidade - mapper struct
        return atividadeUnivesp;
    }
}

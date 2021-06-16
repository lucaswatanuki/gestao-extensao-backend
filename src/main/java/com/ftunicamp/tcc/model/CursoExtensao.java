package com.ftunicamp.tcc.model;

import com.ftunicamp.tcc.controllers.request.Participacao;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Entity(name = "curso_extensao")
@DiscriminatorValue("curso")
public class CursoExtensao extends Atividade {

    private String disciplinaParticipacao;

    private int totalHorasMinistradas;

    private int totalHorasOutrasAtividades;

    private Double valorBrutoHora = 0.0;

    private Double valorBrutoTotal = 0.0;

    @Enumerated(EnumType.STRING)
    private Participacao participacao;

    private String instituicaoVinculada;
}

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
public class CursoExtensaoEntity extends Atividade {

    private String disciplinaParticipacao;

    private int cargaHorariaTotalMinistrada;

    private int cargaHorariaTotalDedicada;

    private Double valorBrutoOutraAtividade;

    private Double valorBrutoHoraAula;

    private Double valorBrutoTotalAulas;

    @Enumerated(EnumType.STRING)
    private Participacao participacao;

    private String instituicaoVinculada;
}

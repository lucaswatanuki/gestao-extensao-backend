package com.ftunicamp.tcc.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "regencia")
@DiscriminatorValue("regencia")
public class RegenciaEntity extends Atividade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String instituicao;

    @Enumerated(EnumType.STRING)
    private Nivel nivel;

    private String curso;

    private String disciplinaParticipacao;

    private boolean responsavel;

    private boolean unicoDocente;

    private int cargaHorariaTotalMinistrada;

    private int cargaHorariaTotalDedicada;

    private Double valorBrutoOutraAtividade;

    private Double valorBrutoHoraAula;

    private Double valorBrutoTotalAulas;

    private int diasTrabalhadosUnicamp;

    private int diasTrabalhadosOutraInstituicao;

    private DiaSemana diasUnicamp;

    private DiaSemana diasOutraInstituicao;

}

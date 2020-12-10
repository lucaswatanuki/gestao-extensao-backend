package com.ftunicamp.tcc.controllers.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class CursoExtensaoRequest implements Serializable {

    private String nomeCurso;
    private Participacao participacao;
    private String disciplina;
    private int cargaHoraTotalMinistrada;
    private Double valorBrutoHoraAula;
    private Double valorBrutoTotalAula;
    private Double valorBrutoOutraAtividade;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private int horaSemanal;
    private int horaMensal;

}

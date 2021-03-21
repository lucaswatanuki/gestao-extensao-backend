package com.ftunicamp.tcc.controllers.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class CursoExtensaoRequest implements Serializable {

    private String nomeCurso;
    private String coordenador;
    private String participacao;
    private String disciplinas;
    private int cargaHoraTotalMinistrada;
    private Double valorBrutoHoraAula = 0.0;
    private Double valorBrutoTotalAula = 0.0;
    private Double valorBrutoOutraAtividade = 0.0;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private int horaSemanal;
    private int horaMensal;
    private String observacao;


}

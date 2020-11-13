package com.ftunicamp.tcc.controllers.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CursoExtensaoRequest implements Serializable {

    private Participacao participacao;
    private List<String> disciplinas;
    private int cargaHoraTotal;
    private Double valorBrutoHoraAula;
    private Double valorBrutoTotalAula;
    private Double valorBrutoOutrasAtividades;
    private int periodo;

}

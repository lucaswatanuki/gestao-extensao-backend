package com.ftunicamp.tcc.controllers.request;

import com.ftunicamp.tcc.model.Nivel;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegenciaRequest {
    private Nivel nivel;
    private String curso;
    private String coordenador;
    private String disciplinaParticipacao;
    private int cargaHoraTotalMinistrada;
    private int cargaHorariaTotalDedicada;
    private Double valorBrutoHoraAula = 0.0;
    private Double valorBrutoTotalAula = 0.0;
    private Double valorBrutoOutraAtividade = 0.0;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private int horaSemanal;
    private int horaMensal;
    private String observacao;
    private String instituicao;
    private String diasTrabalhadosUnicamp;
    private String diasTrabalhadosOutraInstituicao;
    private boolean responsavel;
    private boolean unicoDocente;
}

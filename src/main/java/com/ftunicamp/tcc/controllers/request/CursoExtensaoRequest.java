package com.ftunicamp.tcc.controllers.request;

import com.ftunicamp.tcc.dto.AlocacaoDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CursoExtensaoRequest {

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
    private String instituicaoVinculada;
    private List<AlocacaoDto> alocacoes;
}

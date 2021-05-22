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
public class ConvenioRequest {

    private String projeto;
    private String coordenador;
    private String instituicao;
    private int horaSemanal;
    private int horaMensal;
    private String descricao;
    private int prazo;
    private Double valorBruto;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String observacao;
    private String tipoAtividadeSimultanea;
    private List<AlocacaoDto> alocacoes;
}

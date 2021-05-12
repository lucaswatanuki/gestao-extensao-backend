package com.ftunicamp.tcc.controllers.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ftunicamp.tcc.dto.AlocacaoDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
public class ConvenioRequest implements Serializable {

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
    @JsonProperty("alocacoes")
    private transient List<AlocacaoDto> alocacoes;
}

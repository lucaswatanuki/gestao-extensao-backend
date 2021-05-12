package com.ftunicamp.tcc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ftunicamp.tcc.model.StatusAutorizacao;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize(builder = AlocacaoDto.AlocacaoDtoBuilder.class)
public class AlocacaoDto {

    private long id;
    private int ano;
    private int semestre;
    private long horasAprovadas;
    private long horasSolicitadas;
    private String tipoAtividade;
    private StatusAutorizacao status;

}

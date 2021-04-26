package com.ftunicamp.tcc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlocacaoDto {

    private int ano;
    private int semestre;
    private long horasAprovadas;
    private long horasSolicitadas;
    private String tipoAtividade;

}

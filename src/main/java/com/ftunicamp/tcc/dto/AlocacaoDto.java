package com.ftunicamp.tcc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ftunicamp.tcc.model.StatusAutorizacao;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlocacaoDto {

    private long id;
    private int ano;
    private int semestre;
    private long horasAprovadas;
    private long horasSolicitadas;
    private String tipoAtividade;
    private StatusAutorizacao status;

}

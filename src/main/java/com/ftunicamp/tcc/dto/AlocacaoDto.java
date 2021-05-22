package com.ftunicamp.tcc.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ftunicamp.tcc.model.StatusAutorizacao;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlocacaoDto {

    private long id;
    private long atividadeId;
    private int ano;
    private int semestre;
    private long horasAprovadas;
    private long horasSolicitadas;
    private String tipoAtividade;
    private StatusAutorizacao status;

}

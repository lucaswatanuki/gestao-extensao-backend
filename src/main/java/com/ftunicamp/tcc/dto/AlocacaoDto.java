package com.ftunicamp.tcc.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlocacaoDto {

    private int ano;
    private int semestre;
    private long horasAprovadas;
    private String tipoAtividade;

}

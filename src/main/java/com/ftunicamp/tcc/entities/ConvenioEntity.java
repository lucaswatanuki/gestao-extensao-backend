package com.ftunicamp.tcc.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConvenioEntity extends Atividade{

    private String descricao;
    private String instituicao;
    private String dataRecebimento;
    private int parcelas;

}

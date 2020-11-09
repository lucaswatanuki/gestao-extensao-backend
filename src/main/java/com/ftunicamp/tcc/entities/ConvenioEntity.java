package com.ftunicamp.tcc.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "convenio")
@DiscriminatorValue("convenio")
public class ConvenioEntity extends Atividade{

    private String descricao;
    private String instituicao;
    private String dataRecebimento;
    private int parcelas;

}

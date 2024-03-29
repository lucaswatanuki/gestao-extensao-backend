package com.ftunicamp.tcc.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "convenio")
@DiscriminatorValue("convenio")
public class Convenio extends Atividade{

    private String descricao;
    private String instituicao;
    private String dataRecebimento;
    private int parcelas;
    private String tipoAtividadeSimultanea;

}

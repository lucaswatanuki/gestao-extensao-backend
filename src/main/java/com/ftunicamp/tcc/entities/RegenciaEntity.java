package com.ftunicamp.tcc.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity(name = "regencia")
@DiscriminatorValue("regencia")
public class RegenciaEntity extends Atividade {
}

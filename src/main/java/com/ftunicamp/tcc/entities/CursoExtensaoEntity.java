package com.ftunicamp.tcc.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity(name = "curso_extensao")
@DiscriminatorValue("curso")
public class CursoExtensaoEntity extends Atividade {
}

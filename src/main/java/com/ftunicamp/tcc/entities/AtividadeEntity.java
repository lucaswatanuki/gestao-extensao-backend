package com.ftunicamp.tcc.entities;

import javax.persistence.*;

@Entity
@Table(name = "Atividade")
public class AtividadeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

}

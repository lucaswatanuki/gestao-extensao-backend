package com.ftunicamp.tcc.entities;

import javax.persistence.*;

@Entity
@Table(name = "Docente")
public class DocenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}

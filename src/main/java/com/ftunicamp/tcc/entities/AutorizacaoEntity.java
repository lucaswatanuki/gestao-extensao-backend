package com.ftunicamp.tcc.entities;

import javax.persistence.*;

@Entity
@Table(name = "Autorizacao")
public class AutorizacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}

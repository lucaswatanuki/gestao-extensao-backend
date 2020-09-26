package com.ftunicamp.tcc.entities;

import javax.persistence.*;

@Entity
@Table(name = "Atividade")
public class AtividadeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    private Double valorBruto;

    @ManyToOne
    private DocenteEntity idDocente;

    private Integer horaMensal;

    private Integer horaSemanal;
}

package com.ftunicamp.tcc.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "Atividade")
public class AtividadeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    private Double valorBruto;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DocenteEntity docente;

    private Integer horaMensal;

    private Integer horaSemanal;
}

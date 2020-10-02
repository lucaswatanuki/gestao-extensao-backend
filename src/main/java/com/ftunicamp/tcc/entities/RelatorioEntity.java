package com.ftunicamp.tcc.entities;

import javax.persistence.*;

@Entity
@Table(name = "Relatorio")
public class RelatorioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AtividadeEntity atividade;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DocenteEntity docente;
}

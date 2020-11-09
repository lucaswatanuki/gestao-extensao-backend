package com.ftunicamp.tcc.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="TIPO_ATIVIDADE")
public abstract class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projeto;

    private Double valorBruto;

    private String coordenador;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DocenteEntity docente;

    private Integer horaMensal;

    private Integer horaSemanal;

    private int prazo;

    private LocalDate dataCriacao;

    private LocalDate dataModificacao;

    private boolean urgente;
}

package com.ftunicamp.tcc.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TIPO_ATIVIDADE")
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

    private long prazo;

    @Column(nullable = false)
    private LocalDateTime dataInicio;

    @Column(nullable = false)
    private LocalDateTime dataFim;

    private LocalDate dataCriacao;

    private LocalDate dataModificacao;

    private boolean urgente;

    @Enumerated(EnumType.STRING)
    private StatusAtividade status;

    private String observacao;

    private String revisao;

    @OneToOne(mappedBy = "atividade", cascade = CascadeType.ALL)
    private Alocacao alocacao;

    @Transient
    public String getTipoAtividade() {
        return this.getClass().getAnnotation(DiscriminatorValue.class).value().toUpperCase();
    }
}

package com.ftunicamp.tcc.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "alocacao")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alocacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "ano")
    private int ano;

    @Column(name = "semestre")
    private int semestre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="docente_id")
    private Docente docente;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "atividade_id", referencedColumnName = "id")
    private Atividade atividade;

    private long totalHorasAprovadas;

    private long totalHorasSolicitadas;
}

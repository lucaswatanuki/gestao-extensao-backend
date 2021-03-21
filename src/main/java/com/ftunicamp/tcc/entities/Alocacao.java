package com.ftunicamp.tcc.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Alocacao")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alocacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ano")
    private int ano;

    @Column(name = "semestre")
    private int semestre;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="docente_id")
    private DocenteEntity docente;

    @OneToOne
    @JoinColumn(name = "atividade_id", referencedColumnName = "id")
    private Atividade atividade;

    private long totalHorasAprovadas;

    private long totalHorasSolicitadas;
}

package com.ftunicamp.tcc.model;

import com.ftunicamp.tcc.model.Atividade;
import com.ftunicamp.tcc.model.DocenteEntity;
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
    private Integer id;

    @Column(name = "ano")
    private int ano;

    @Column(name = "semestre")
    private int semestre;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="docente_id")
    private DocenteEntity docente;

    @ManyToOne
    @JoinColumn(name = "atividade_id", referencedColumnName = "id")
    private Atividade atividade;

    private long totalHorasAprovadas;

    private long totalHorasSolicitadas;
}

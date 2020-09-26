package com.ftunicamp.tcc.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Autorizacao")
public class AutorizacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private StatusAutorizacao status;

    private LocalDate date;

    @ManyToOne
    private AtividadeEntity idAtividade;

    @ManyToOne
    private DocenteEntity aprovador;

}

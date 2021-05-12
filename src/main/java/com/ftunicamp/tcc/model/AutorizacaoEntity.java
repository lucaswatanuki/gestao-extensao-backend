package com.ftunicamp.tcc.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "autorizacao")
@Getter
@Setter
public class AutorizacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private StatusAutorizacao status;

    private LocalDate data;

    @OneToOne(fetch = FetchType.EAGER)
    private Atividade atividade;

    private String docente;

}

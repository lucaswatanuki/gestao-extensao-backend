package com.ftunicamp.tcc.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Docente")
public class DocenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nome;

    private String cpf;

    private String rf;

    private String endereco;

    @OneToMany
    private List<AtividadeEntity> atividades;

    private String email;

    private String telefone;

    private LocalDate dtNascimento;
}

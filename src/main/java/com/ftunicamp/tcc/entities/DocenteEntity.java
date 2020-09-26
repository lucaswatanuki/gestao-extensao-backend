package com.ftunicamp.tcc.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
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

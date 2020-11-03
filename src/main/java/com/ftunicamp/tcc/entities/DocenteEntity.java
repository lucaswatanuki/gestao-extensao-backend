package com.ftunicamp.tcc.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private List<AtividadeEntity> atividades;

    private String email;

    private String telefone;

    private boolean autorizado;

    private int totalHoras;

}

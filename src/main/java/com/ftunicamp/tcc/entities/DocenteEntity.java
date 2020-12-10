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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String matricula;

    private String endereco;

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private List<Atividade> atividades;

    @OneToOne
    private UsuarioEntity user;

    private String email;

    private String telefone;

    private boolean autorizado;

    private int totalHoras;

}

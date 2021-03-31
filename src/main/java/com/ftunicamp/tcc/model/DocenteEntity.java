package com.ftunicamp.tcc.model;

import lombok.*;

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

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Atividade> atividades;

    @OneToOne
    private UsuarioEntity user;

    private String email;

    private String telefone;

    private boolean autorizado;

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private List<Alocacao> alocacao;

    @Entity
    @Table(name = "Alocacao")
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Alocacao {
        @Id
        @GeneratedValue
        @Column(name = "id")
        private Integer id;

        @Column(name = "ano")
        private int ano;

        @Column(name = "semestre")
        private int semestre;

        @ManyToOne(fetch = FetchType.LAZY)
        private DocenteEntity docente;

        private long totalHorasAprovadas;

        private long totalHorasSolicitadas;
    }
}

package com.ftunicamp.tcc.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Anexo")
public class AnexoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Atividade atividade;

    private String path;

    private String nomeArquivo;

    private LocalDateTime criadoEm;

    private String criadoPor;
}

package com.ftunicamp.tcc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "arquivos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Arquivo {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private long atividadeId;

    private String path;

    private String nome;

    private String tipo;

    @Lob
    private byte[] data;

    public Arquivo(String nome, String tipo, byte[] data, long atividadeId) {
        this.nome = nome;
        this.tipo = tipo;
        this.data = data;
        this.atividadeId = atividadeId;
    }
}

package com.ftunicamp.tcc.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "profiles")
@Getter
@Setter
public class ProfilesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Profiles name;

    public ProfilesEntity() {

    }

    public ProfilesEntity(Profiles name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Profiles getName() {
        return name;
    }

    public void setName(Profiles name) {
        this.name = name;
    }
}

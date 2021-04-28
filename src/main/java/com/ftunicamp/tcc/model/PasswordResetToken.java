package com.ftunicamp.tcc.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Data
public class PasswordResetToken {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = UsuarioEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UsuarioEntity user;

    private Date expiryDate;

    public PasswordResetToken(String token, UsuarioEntity user) {
        this.token = token;
        this.user = user;
    }
}

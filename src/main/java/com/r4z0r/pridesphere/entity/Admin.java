package com.r4z0r.pridesphere.entity;

import com.r4z0r.pridesphere.Util;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(updatable = false)
    private String username = new Util("==").gerarCodigo(12); //permissao gerada

    @Column(updatable = false)
    private Date dataCadastro = new Date();
    private Date dataAtivacao;

    //informacoes vindas do auth0
    private String email;
    private String familyName;
    private String givenName;
    private String name;
    private String nickname;
    private String picture;
    private String phoneNumber;

    private boolean ativo = false; //usuario ativo
    private boolean valido = true; //valido para criacao de usuario no auth0
}

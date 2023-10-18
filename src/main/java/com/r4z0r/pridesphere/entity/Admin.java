package com.r4z0r.pridesphere.entity;

import com.r4z0r.pridesphere.Util;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    private String validationCode = new Util("==").gerarCodigo(12); //permissao gerada

    @Column(updatable = false)
    private LocalDateTime dataCadastro;

    private Date dataAtivacao;
    private LocalDateTime dataAtualizacao;

    private String email;
    private String name;
    private String nickname;
    private byte[] picture;

    private boolean ativo = false; //usuario ativo
    private boolean valido = true;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @PrePersist
    public void prePersist() {
        dataCadastro = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}

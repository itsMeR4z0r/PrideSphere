package com.r4z0r.pridesphere.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AdminSession {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(updatable = false)
    private LocalDateTime dataCadastro;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "adminId", referencedColumnName = "id", nullable = false)
    private Admin admin;
    
    private boolean usado;
    private boolean logado;

    private String ip;
    private String userAgent;
    private String device;
    private String os;
    private String requestId;



    @PrePersist
    public void prePersist() {
        logado = false;
        usado = false;
        dataCadastro = LocalDateTime.now();
    }


}

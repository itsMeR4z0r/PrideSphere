package com.r4z0r.pridesphere.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Admin admin;

    @Column(updatable = false)
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    @NotNull
    @NotBlank
    private Long idPlataforma;

    @NotNull
    @NotBlank
    private Long chatId;
    @PrePersist
    public void prePersist() {
        dataCadastro = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}

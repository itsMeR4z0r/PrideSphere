package com.r4z0r.pridesphere.entity;

import com.r4z0r.pridesphere.enums.Plataforma;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;
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

    private String nome;
    @NotNull
    private Plataforma plataforma;

    @Column(updatable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dataCadastro;
    @NotNull
    @NotBlank
    private String idPlataforma;

    @PrePersist
    public void onInsert() {
        dataCadastro = new Timestamp(System.currentTimeMillis());
    }
}

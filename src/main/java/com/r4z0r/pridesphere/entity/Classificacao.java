package com.r4z0r.pridesphere.entity;

import com.r4z0r.pridesphere.enums.TipoClassificacao;
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
public class Classificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @NotBlank
    private String descricao;

    @NotNull
    @Column(updatable = false)
    private TipoClassificacao tipoClassificacao;

    @Column(updatable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dataCriacao;

    @Column(insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dataAtualizacao;

    @PrePersist
    public void onInsert() {
        dataCriacao = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    public void onUpdate() {
        dataAtualizacao = new Timestamp(System.currentTimeMillis());
    }

}

package com.r4z0r.pridesphere.entity;

import jakarta.persistence.*;
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
public class Relato {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(updatable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp dataCadastro;
    @OneToOne
    private Usuario usuario;
    @OneToOne
    private Classificacao classificacao;
    @NotNull
    private int intensidade;
    private String pais;
    private String paisCode;
    private String estado;
    private String estadoCode;
    private String regiao;
    private String cidade;
    private String codigoPostal;
    private String bairro;
    private String rua;
    private String housenumber;
    private Double lon;
    private Double lat;

    @PrePersist
    public void onInsert() {
        dataCadastro = new Timestamp(System.currentTimeMillis());
    }

}

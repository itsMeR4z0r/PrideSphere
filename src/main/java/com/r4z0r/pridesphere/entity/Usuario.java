package com.r4z0r.pridesphere.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

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

    @Column(updatable = false)
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    @NotNull
    private Long idPlataforma;

    @NotNull
    private Long chatId;

    @PrePersist
    public void prePersist() {
        dataCadastro = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    @Override
    public String toString() {
        try {
            var json = new JSONObject();
            json.put("id", id);
            json.put("username", username);
            json.put("dataCadastro", dataCadastro);
            json.put("dataAtualizacao", dataAtualizacao);
            json.put("idPlataforma", idPlataforma);
            json.put("chatId", chatId);
            return json.toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}

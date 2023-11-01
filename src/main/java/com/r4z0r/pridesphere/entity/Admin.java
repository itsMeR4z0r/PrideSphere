package com.r4z0r.pridesphere.entity;

import com.r4z0r.pridesphere.Util;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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

    private String validationCode = Util.gerarCodigo(12); //permissao gerada

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

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdminSession> sessions;
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
            json.put("validationCode", validationCode);
            json.put("dataCadastro", dataCadastro);
            json.put("dataAtualizacao", dataAtualizacao);
            json.put("dataAtivacao", dataAtivacao);
            json.put("email", email);
            json.put("name", name);
            json.put("nickname", nickname);
            json.put("picture", picture);
            json.put("ativo", ativo);
            json.put("valido", valido);
            json.put("usuario", usuario != null ? new JSONObject(usuario.toString()) : null);
            json.put("sessions", sessions);
            return json.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.r4z0r.pridesphere.bot.data;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CallbackMsg {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String etapa;
    private String natureza;
    private String relatoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "messageId", referencedColumnName = "messageId", nullable = false)
    private Mensagem mensagem;

}

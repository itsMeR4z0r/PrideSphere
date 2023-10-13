package com.r4z0r.pridesphere.bot.data;

import com.r4z0r.pridesphere.entity.Classificacao;
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
    
    private Double lon;
    private Double lat;

    private int intensidade;

    @OneToOne
    private Classificacao classificacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "messageId", referencedColumnName = "messageId", nullable = false)
    private Mensagem mensagem;

}

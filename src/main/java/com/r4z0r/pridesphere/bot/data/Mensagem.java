package com.r4z0r.pridesphere.bot.data;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Mensagem {
    @Id
    private Integer messageId;

    private Long chatId;

    private Long userId;

    private String userName;

    @OneToMany(mappedBy = "mensagem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CallbackMsg> callbackMsgs = new ArrayList<>();
}

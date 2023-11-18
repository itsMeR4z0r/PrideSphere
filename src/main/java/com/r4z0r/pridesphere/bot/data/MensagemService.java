package com.r4z0r.pridesphere.bot.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MensagemService {
    @Autowired
    private MensagemRepository mensagemRepository;

    public Mensagem save(Mensagem mensagem) {
        return mensagemRepository.save(mensagem);
    }

    public boolean existsById(Integer messageId) {
        return mensagemRepository.existsById(messageId);
    }

    public Optional<Mensagem> findById(Integer messageId) {
        return mensagemRepository.findById(messageId);
    }
}

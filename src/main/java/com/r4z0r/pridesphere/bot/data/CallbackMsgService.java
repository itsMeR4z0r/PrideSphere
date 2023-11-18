package com.r4z0r.pridesphere.bot.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class CallbackMsgService {
    @Autowired
    private CallbackMsgRepository callbackMsgRepository;

    public boolean existsById(UUID uuid) {
        return callbackMsgRepository.existsById(uuid);
    }

    public Optional<CallbackMsg> findById(UUID uuid) {
        return callbackMsgRepository.findById(uuid);
    }

    public CallbackMsg save(CallbackMsg relato) {
        return callbackMsgRepository.save(relato);
    }
    @Transactional
    public void deleteByMessageId(Integer messageId) {
        callbackMsgRepository.deleteByMessageId(messageId);
    }
}

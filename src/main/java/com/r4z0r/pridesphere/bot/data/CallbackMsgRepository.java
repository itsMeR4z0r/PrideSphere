package com.r4z0r.pridesphere.bot.data;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface CallbackMsgRepository extends CrudRepository<CallbackMsg, UUID> {
    @Transactional
    @Modifying
    @Query("delete FROM CallbackMsg c WHERE c.mensagem.messageId = ?1")
    void deleteByMessageId(Integer messageId);
}

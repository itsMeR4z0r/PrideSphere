package com.r4z0r.pridesphere.bot.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagemRepository extends CrudRepository<Mensagem, Integer> {
}

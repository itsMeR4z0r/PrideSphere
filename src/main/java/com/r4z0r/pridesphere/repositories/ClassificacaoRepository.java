package com.r4z0r.pridesphere.repositories;

import com.r4z0r.pridesphere.entity.Classificacao;
import com.r4z0r.pridesphere.enums.TipoClassificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ClassificacaoRepository extends JpaRepository<Classificacao, UUID> {
    @Query("select c from Classificacao c where c.tipoClassificacao = ?1")
    List<Classificacao> findByTipoClassificacao(TipoClassificacao tipo);

    @Query("select c from Classificacao c where c.tipoClassificacao = ?1")
    List<Classificacao> findByNatureza(TipoClassificacao natureza);
}
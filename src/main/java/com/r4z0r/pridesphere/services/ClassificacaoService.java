package com.r4z0r.pridesphere.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.r4z0r.pridesphere.entity.Classificacao;
import com.r4z0r.pridesphere.enums.TipoClassificacao;
import com.r4z0r.pridesphere.repositories.ClassificacaoRepository;


@Service
public class ClassificacaoService {
    private final ClassificacaoRepository classificacaoRepository;

    @Autowired
    public ClassificacaoService(ClassificacaoRepository classificacaoRepository) {
        this.classificacaoRepository = classificacaoRepository;
    }

    public Optional<Classificacao> findById(UUID id) {
        return classificacaoRepository.findById(id);
    }

    public Classificacao save(Classificacao classificacao) {
        validateClassificacao(classificacao);
        return classificacaoRepository.save(classificacao);
    }

    public List<Classificacao> findByTipoClassificacao(TipoClassificacao tipo) {
        return classificacaoRepository.findByTipoClassificacao(tipo);
    }

    @Transactional
    public void deleteById(UUID id) {
        classificacaoRepository.deleteById(id);
    }

    private void validateClassificacao(Classificacao classificacao) {
        Optional.ofNullable(classificacao)
                .map(Classificacao::getTipoClassificacao)
                .orElseThrow(() -> new RuntimeException("TipoClassificacao cannot be null"));

        Optional.ofNullable(classificacao)
                .map(Classificacao::getDescricao)
                .filter(descricao -> !descricao.isBlank())
                .orElseThrow(() -> new RuntimeException("Descricao cannot be null or blank"));
    }

    public List<Classificacao> findByNatureza(TipoClassificacao natureza) {
        return classificacaoRepository.findByNatureza(natureza);
    }
}

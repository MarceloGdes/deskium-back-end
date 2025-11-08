package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Prioridade;
import br.deskiumcompany.deskium_ai_api.respository.PrioridadeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrioridadeService {
    @Autowired
    private PrioridadeRepository repository;

    public List<Prioridade> getAll() {
        return repository.findAll();
    }

    public Prioridade getById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prioridade não encontrada."));
    }
}

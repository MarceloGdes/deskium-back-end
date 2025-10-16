package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Motivo;
import br.deskiumcompany.deskium_ai_api.respository.MotivoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MotivoService {

    @Autowired
    private MotivoRepository repository;

    public List<Motivo> getAll() {
        return repository.findAll();
    }

    public Motivo getById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Motivo não encontrado."));
    }
}

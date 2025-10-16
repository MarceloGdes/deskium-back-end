package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Categoria;
import br.deskiumcompany.deskium_ai_api.respository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    public List<Categoria> getAll() {
        return repository.findAll();
    }

    public Categoria getById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrado."));
    }
}

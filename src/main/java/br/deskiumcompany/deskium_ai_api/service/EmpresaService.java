package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Empresa;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.respository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository repository;

    public Empresa insert(Empresa empresa) throws BussinesException {
        if(repository.existsByCnpj(empresa.getCnpj()))
            throw new BussinesException("Cnpj já cadastrado no sistema. Verifique.");
        if(repository.existsByEmail(empresa.getEmail()))
            throw new BussinesException("E-mail já cadastrado no sistema. Verifique.");

        return repository.save(empresa);
    }

    public Empresa getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada."));
    }
}

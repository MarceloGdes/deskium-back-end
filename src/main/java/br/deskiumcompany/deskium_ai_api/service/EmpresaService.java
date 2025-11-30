package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Empresa;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.respository.EmpresaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Empresa> findAll(Long id, String razaoSocial, String cnpj) {
        return repository.findByFiltros(id, razaoSocial, cnpj);
    }

    public Empresa getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empresa não encontrada."));
    }

    public Empresa update(Long id, Empresa newEmpresa) throws BussinesException {
        Empresa empresa = getById(id);

        if(!empresa.getCnpj().equals(newEmpresa.getCnpj()) && repository.existsByCnpj(newEmpresa.getCnpj()))
            throw new BussinesException("Cnpj já cadastrado no sistema. Verifique.");

        if(!empresa.getEmail().equals(newEmpresa.getEmail()) && repository.existsByEmail(newEmpresa.getEmail()))
            throw new BussinesException("E-mail já cadastrado no sistema. Verifique.");

        empresa.setRazaoSocial(newEmpresa.getRazaoSocial());
        empresa.setCnpj(newEmpresa.getCnpj());
        empresa.setEmail(newEmpresa.getEmail());
        empresa.setTelefone(newEmpresa.getTelefone());
        empresa.setObservacoes(newEmpresa.getObservacoes());
        empresa.setAtivo(newEmpresa.isAtivo());

        return repository.save(empresa);
    }
}

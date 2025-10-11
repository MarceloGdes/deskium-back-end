package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.respository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository repository;

    protected boolean existsByEmail(String email){
        return repository.existsByEmailAndAtivoIsTrue(email);
    }

    public UserDetails findByLogin(String email){
        return repository.findByEmailAndAtivoIsTrue(email);
    }
}

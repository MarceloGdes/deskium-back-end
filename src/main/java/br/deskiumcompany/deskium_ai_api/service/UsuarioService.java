package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.respository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Classe utilizada pelo SpringSecurity
//https://www.youtube.com/watch?v=5w-YCcOjPD0
@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository repository;

    protected boolean existsByEmail(String email){
        return repository.existsByEmailAndAtivoIsTrue(email);
    }

    protected Usuario findByEmail(String email){
        return (Usuario) repository.findByEmailAndAtivoIsTrue(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var userDetails = repository.findByEmailAndAtivoIsTrue(email);

        if(userDetails == null)
            throw new UsernameNotFoundException("E-mail não encontrado.");

        return userDetails;
    }
}

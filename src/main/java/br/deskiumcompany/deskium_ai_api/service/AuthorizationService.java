package br.deskiumcompany.deskium_ai_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//Classe utilizada pelo SpringSecurity
//https://www.youtube.com/watch?v=5w-YCcOjPD0
@Service
public class AuthorizationService implements UserDetailsService {
    @Autowired
    UsuarioService service;
    @Override
    //Metodo utilizado para consultar o usuário
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails =  service.findByLogin(email);

        if(userDetails == null)
            throw new UsernameNotFoundException("E-mail não encontrado.");

        return userDetails;
    }
}

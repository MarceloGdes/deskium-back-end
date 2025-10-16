package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Suporte;
import br.deskiumcompany.deskium_ai_api.domain.enums.TipoUsuario;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.respository.SuporteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SuporteService {

    @Autowired
    private SuporteRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Suporte insert(Suporte suporte) throws EntityNotFoundException, BussinesException {
        if(usuarioService.findByEmail(suporte.getUsuario().getEmail()) != null)
            throw new BussinesException("Já existe um usuário ativo com o mesmo e-mail.");


        var tipoUsuario = suporte.getUsuario().getTipoUsuario();
        if(tipoUsuario != TipoUsuario.GESTOR_SUPORTE && tipoUsuario != TipoUsuario.SUPORTE)
            throw new BussinesException("Tipo de usuário para suporte inválido. Verifique");

        suporte.getUsuario().setSenha(passwordEncoder.encode(suporte.getUsuario().getSenha()));
        suporte.getUsuario().setAtivo(true);

        return repository.save(suporte);

    }

    public Suporte getById(Long id) throws EntityNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Suporte não encontrado."));
    }

    public Suporte getSuporteComMenosTickets(){
        var filaSuportes =  repository.findSuportesOrderByTicketsAbertosAsc();

        if(!filaSuportes.isEmpty()){
            return filaSuportes.getFirst();
        }

        throw new EntityNotFoundException("Nenhum suporte ativo disponível. Contate o administrador do sistema.");
    }
}

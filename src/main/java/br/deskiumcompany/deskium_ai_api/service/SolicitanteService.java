package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Empresa;
import br.deskiumcompany.deskium_ai_api.domain.Solicitante;
import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.domain.enums.TipoUsuario;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.respository.SolicitanteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SolicitanteService {

    @Autowired
    private SolicitanteRepository repository;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private UsuarioService usuarioService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Solicitante insert(Solicitante solicitante) throws EntityNotFoundException, BussinesException {
        try {
            Empresa empresa = empresaService
                    .getById(solicitante.getEmpresa().getId());

            if(empresa == null)
                throw new EntityNotFoundException("Empresa não encontrada.");

            if(usuarioService.existsByEmail(solicitante.getUsuario().getEmail()))
                throw new BussinesException("Já existe um usuário ativo com o mesmo e-mail.");

            solicitante.setEmpresa(empresa);
            solicitante.getUsuario().setTipoUsuario(TipoUsuario.SOLICITANTE);
            solicitante.getUsuario().setSenha(passwordEncoder
                    .encode(solicitante.getUsuario().getSenha()));

            return repository.save(solicitante);

        }catch (EntityNotFoundException e) {
            throw e;

        }catch (BussinesException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Empresa;
import br.deskiumcompany.deskium_ai_api.domain.Solicitante;
import br.deskiumcompany.deskium_ai_api.domain.enums.TipoUsuario;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.respository.SolicitanteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitanteService {

    @Autowired
    private SolicitanteRepository repository;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Solicitante insert(Solicitante solicitante) throws EntityNotFoundException, BussinesException {
        try {
            Empresa empresa = empresaService
                    .getById(solicitante.getEmpresa().getId());

            if(usuarioService.findByEmail(solicitante.getUsuario().getEmail()) != null)
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


    public Solicitante getById(Long id) throws EntityNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitante não encontrado"));
    }

    public Solicitante update(Long id, Solicitante solicitanteAtualizado) throws EntityNotFoundException, BussinesException {
        try {
            Solicitante solicitante = getById(id);

            solicitante.getUsuario()
                    .setNomeCompleto(solicitanteAtualizado.getUsuario().getNomeCompleto());
            solicitante.getUsuario()
                    .setAtivo(solicitanteAtualizado.getUsuario().isAtivo());
            solicitante.getUsuario()
                    .setEmail(solicitanteAtualizado.getUsuario().getEmail());

            if(solicitante.getUsuario().getSenha() == null
                    || solicitante.getUsuario().getSenha().isBlank()) {
                solicitante.getUsuario()
                        .setSenha(solicitanteAtualizado.getUsuario().getSenha());
            }

            solicitante.setCargo(solicitanteAtualizado.getCargo());
            solicitante.setSetor(solicitanteAtualizado.getSetor());
            solicitante.setCelular(solicitanteAtualizado.getCelular());
            solicitante.setTelefone(solicitanteAtualizado.getTelefone());
            solicitante.setObservacoes(solicitanteAtualizado.getObservacoes());

            //Valida se o e-mail informado está gravado em outro usuário, que seja diferenet do vinculado ao solicitante.
            var usuario = usuarioService.findByEmail(solicitante.getUsuario().getEmail());
            if(usuario != null && usuario.getId() != solicitante.getUsuario().getId()){
                throw new BussinesException("E-mail informado já está sendo utilizado por outro usuário.");
            }

            return repository.save(solicitante);

        } catch (EntityNotFoundException e) {
            throw e;
        } catch (BussinesException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

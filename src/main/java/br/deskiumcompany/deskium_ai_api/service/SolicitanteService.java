package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Empresa;
import br.deskiumcompany.deskium_ai_api.domain.Solicitante;
import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.domain.enums.TipoUsuario;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.respository.SolicitanteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public Solicitante insert(Solicitante solicitante) throws BussinesException {

        Empresa empresa = empresaService.getById(solicitante.getEmpresa().getId());

        if(usuarioService.findByEmail(solicitante.getUsuario().getEmail()) != null)
            throw new BussinesException("Já existe um usuário ativo com o mesmo e-mail.");

        solicitante.setEmpresa(empresa);
        solicitante.getUsuario().setTipoUsuario(TipoUsuario.SOLICITANTE);
        solicitante.getUsuario().setSenha(passwordEncoder
                .encode(solicitante.getUsuario().getSenha()));

        return repository.save(solicitante);

    }

    public Solicitante getById(Long id) throws EntityNotFoundException {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitante não encontrado"));
    }

    public Solicitante update(Long id, Solicitante solicitanteAtualizado) throws BussinesException {

        Solicitante solicitante = getById(id);
        Usuario usuario = solicitante.getUsuario();

        usuario.setNomeCompleto(solicitanteAtualizado.getUsuario().getNomeCompleto());
        usuario.setAtivo(solicitanteAtualizado.getUsuario().isAtivo());
        usuario.setEmail(solicitanteAtualizado.getUsuario().getEmail());

        if(usuario.getSenha() == null || usuario.getSenha().isBlank()) {
            usuario.setSenha(solicitanteAtualizado.getUsuario().getSenha());
        }

        solicitante.setCargo(solicitanteAtualizado.getCargo());
        solicitante.setSetor(solicitanteAtualizado.getSetor());
        solicitante.setCelular(solicitanteAtualizado.getCelular());
        solicitante.setTelefone(solicitanteAtualizado.getTelefone());
        solicitante.setObservacoes(solicitanteAtualizado.getObservacoes());

        //Valida se o e-mail informado está gravado em outro usuário, que seja diferenet do vinculado ao solicitante.
        var usuarioExistente = usuarioService.findByEmail(solicitante.getUsuario().getEmail());
        if(usuarioExistente != null && usuarioExistente.getId() != solicitante.getUsuario().getId()){
            throw new BussinesException("E-mail informado já está sendo utilizado por outro usuário.");
        }

        solicitante.setUsuario(usuario);

        return repository.save(solicitante);
    }

    public Solicitante getByUsuarioId(Long id){
        return repository.findByUsuarioId(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitante não encontrato. Contate o suporte."));
    }
}

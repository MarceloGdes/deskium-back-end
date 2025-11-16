package br.deskiumcompany.deskium_ai_api.domain;

import br.deskiumcompany.deskium_ai_api.domain.enums.TipoUsuario;
import br.deskiumcompany.deskium_ai_api.dto.suporte.SuporteInsertDTO;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Suporte extends EntidadeBase{
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Suporte(SuporteInsertDTO dto) throws BussinesException {
        try {
            var usuario = new Usuario();

            usuario.setEmail(dto.getEmail());
            usuario.setSenha(dto.getSenha());
            usuario.setNomeCompleto(dto.getNomeCompleto());

            if(dto.getTipoUsuarioSuporte().equals(TipoUsuario.SOLICITANTE))
                throw new BussinesException("Tipo de usuário incálido para suporte e gestor de suporte.");

            usuario.setTipoUsuario(dto.getTipoUsuarioSuporte());

            this.usuario = usuario;

        }catch (IllegalArgumentException e) {
            throw new BussinesException("Tipo do usuário do suporte inválido.");
        }
    }
}

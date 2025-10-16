package br.deskiumcompany.deskium_ai_api.domain;

import br.deskiumcompany.deskium_ai_api.domain.enums.TipoUsuario;
import br.deskiumcompany.deskium_ai_api.dto.suporte.SuporteInsertDTO;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Suporte extends EntidadeBase{
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private Usuario usuario;

    @OneToMany(mappedBy = "suporte")
    private List<Ticket> tickets;

    public Suporte(SuporteInsertDTO dto) throws BussinesException {
        try {
            var usuario = new Usuario();

            usuario.setEmail(dto.getEmail());
            usuario.setSenha(dto.getSenha());
            usuario.setNomeCompleto(dto.getNomeCompleto());

            usuario.setTipoUsuario(TipoUsuario.valueOf(dto.getTipoUsuarioSuporte()));

            this.usuario = usuario;

        }catch (IllegalArgumentException e) {
            throw new BussinesException("Tipo do usuário do suporte inválido.");
        }
    }
}

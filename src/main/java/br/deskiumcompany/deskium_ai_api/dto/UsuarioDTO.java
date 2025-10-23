package br.deskiumcompany.deskium_ai_api.dto;

import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.domain.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private String nomeCompleto;
    private String email;
    private TipoUsuario tipoUsuario;

    public UsuarioDTO(Usuario usuario) {
        this.nomeCompleto = usuario.getNomeCompleto();
        this.email = usuario.getEmail();
        this.tipoUsuario = usuario.getTipoUsuario();
    }
}



package br.deskiumcompany.deskium_ai_api.dto.suporte;

import br.deskiumcompany.deskium_ai_api.domain.enums.TipoUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SuporteInsertDTO {
    @NotBlank
    @Size(min = 2, max = 100)
    private String nomeCompleto;

    @NotBlank
    @Email
    @Size(min = 5, max = 255)
    private String email;

    @NotBlank
    private String senha;

    @NotNull
    private TipoUsuario tipoUsuarioSuporte;

}

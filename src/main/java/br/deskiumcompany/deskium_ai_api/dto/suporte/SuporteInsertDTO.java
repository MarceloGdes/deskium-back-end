package br.deskiumcompany.deskium_ai_api.dto.suporte;

import br.deskiumcompany.deskium_ai_api.domain.enums.TipoUsuario;
import com.fasterxml.jackson.databind.annotation.EnumNaming;
import jakarta.validation.constraints.*;
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

    @NotBlank
    private String tipoUsuarioSuporte;

}

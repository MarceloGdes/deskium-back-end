package br.deskiumcompany.deskium_ai_api.dto.suporte;

import br.deskiumcompany.deskium_ai_api.domain.Suporte;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SuporteResponseDTO {
    private Long id;
    private String nomeCompleto;
    private String email;

    public SuporteResponseDTO(Suporte suporte) {
        this.id = suporte.getId();
        this.nomeCompleto = suporte.getUsuario().getNomeCompleto();
        this.email = suporte.getUsuario().getEmail();
    }
}

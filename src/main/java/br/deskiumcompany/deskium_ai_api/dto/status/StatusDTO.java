package br.deskiumcompany.deskium_ai_api.dto.status;

import br.deskiumcompany.deskium_ai_api.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusDTO {
    private String id;
    private String descricao;

    public StatusDTO(Status subStatus) {
        this.id = subStatus.name();
        this.descricao = subStatus.getDescricao();
    }
}

package br.deskiumcompany.deskium_ai_api.dto.substatus;

import br.deskiumcompany.deskium_ai_api.domain.enums.SubStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubStatusDTO {
    private String id;
    private String descricao;

    public SubStatusDTO(SubStatus subStatus) {
        this.id = subStatus.name();
        this.descricao = subStatus.getDescricao();
    }
}

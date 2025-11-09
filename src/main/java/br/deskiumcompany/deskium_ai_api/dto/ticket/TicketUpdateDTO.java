package br.deskiumcompany.deskium_ai_api.dto.ticket;

import br.deskiumcompany.deskium_ai_api.domain.enums.SubStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketUpdateDTO {
    @NotNull
    private Long motivoId;
    private Long categoriaId;
    @NotNull
    private SubStatus subStatusId;
    private Long prioridadeId;

}

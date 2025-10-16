package br.deskiumcompany.deskium_ai_api.dto.ticket;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketInsertDTO {
    @NotBlank
    @Size(min = 2, max = 100)
    private String titulo;

    @NotNull
    private Long motivoId;

    @NotNull
    private Long categoriaId;

    @NotBlank
    private String descricaoHtml;
}

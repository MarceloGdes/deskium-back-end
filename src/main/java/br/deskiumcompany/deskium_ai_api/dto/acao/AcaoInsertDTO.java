package br.deskiumcompany.deskium_ai_api.dto.acao;

import br.deskiumcompany.deskium_ai_api.domain.enums.Status;
import br.deskiumcompany.deskium_ai_api.dto.arquivo.ArquivoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcaoInsertDTO {
    private boolean acaoInterna = false;
    private Status statusId = Status.ABERTO;
    @NotBlank
    private String html;
    private List<@Valid ArquivoDTO> anexos;
}

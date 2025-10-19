package br.deskiumcompany.deskium_ai_api.dto.arquivo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArquivoDTO {

    @NotBlank
    private String fileName;
}

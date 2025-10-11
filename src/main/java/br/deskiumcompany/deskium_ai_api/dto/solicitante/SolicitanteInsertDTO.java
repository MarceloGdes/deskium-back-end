package br.deskiumcompany.deskium_ai_api.dto.solicitante;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SolicitanteInsertDTO {
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
    @Size(min = 11, max = 11)
    @Pattern(regexp = "^\\d+$", message = "Deve conter apenas números.")
    private String celular;

    @NotBlank
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^\\d+$", message = "Deve conter apenas números.")
    private String telefone;

    @NotNull
    private Long empresaId;

    @NotBlank
    @Size(min = 2, max = 50)
    private String cargo;

    @NotBlank
    @Size(min = 2, max = 50)
    private String setor;

    @Size(max = 255)
    private String observacoes;

}

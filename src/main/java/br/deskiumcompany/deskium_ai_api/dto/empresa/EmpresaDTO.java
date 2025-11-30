package br.deskiumcompany.deskium_ai_api.dto.empresa;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmpresaDTO {

    @NotBlank
    @Size(min = 2, max = 255)
    private String razaoSocial;

    @NotBlank
    @Size(min = 14, max = 14)
    @CNPJ //Anotation padrão do bean validation. Validia se é um CNPJ valido
    private String cnpj;

    @NotBlank
    @Email
    @Size(min = 5, max = 255)
    private String email;

    @NotBlank
    @Size(min = 10, max = 10)
    @Pattern(regexp = "^\\d+$", message = "Deve conter apenas números.")
    private String telefone;

    @Size(max = 255)
    private String observacoes;

    private boolean ativo;
}

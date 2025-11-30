package br.deskiumcompany.deskium_ai_api.domain;

import br.deskiumcompany.deskium_ai_api.dto.empresa.EmpresaDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Empresa extends EntidadeBase {
    @Column(nullable = false)
    private String razaoSocial;

    @Column(nullable = false, length = 14, unique = true)
    private String cnpj;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 10)
    private String telefone;

    private String observacoes;

    @Column(nullable = false)
    private boolean ativo = true;

    public Empresa(EmpresaDTO dto) {
        this.razaoSocial = dto.getRazaoSocial();
        this.cnpj = dto.getCnpj();
        this.email = dto.getEmail();
        this.telefone = dto.getTelefone();
        this.observacoes = dto.getObservacoes();
        this.ativo = dto.isAtivo();
    }
}

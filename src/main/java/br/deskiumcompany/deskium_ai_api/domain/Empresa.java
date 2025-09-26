package br.deskiumcompany.deskium_ai_api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Column(nullable = false, length = 12)
    private String telefone;

    private String observacoes;

    @OneToMany(mappedBy = "empresa")
    private List<Solicitante> solicitantes;
}

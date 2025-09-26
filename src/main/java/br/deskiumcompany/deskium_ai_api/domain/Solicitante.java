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
public class Solicitante extends EntidadeBase{
    @Column(nullable = false, length = 50)
    private String cargo;

    @Column(nullable = false, length = 50)
    private String setor;

    @Column(length = 14)
    private String celular;

    @Column(nullable = false, length = 14)
    private String telefone;

    private String observacoes;

    @ManyToOne(optional = false)
    private Empresa empresa;

    @OneToOne(optional = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "solicitante")
    private List<Ticket> tickets;
}

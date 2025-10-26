package br.deskiumcompany.deskium_ai_api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Anexo extends EntidadeBase {
    @ManyToOne(optional = false)
    @JoinColumn(name = "acao_id")
    private Acao acao;

    @Column(nullable = false)
    private String fileName;
}

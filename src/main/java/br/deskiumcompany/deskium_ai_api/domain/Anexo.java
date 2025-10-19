package br.deskiumcompany.deskium_ai_api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
    private Acao acao;

    @Column(nullable = false)
    private String fileName;
}

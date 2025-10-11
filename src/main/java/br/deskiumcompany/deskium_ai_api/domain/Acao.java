package br.deskiumcompany.deskium_ai_api.domain;

import br.deskiumcompany.deskium_ai_api.domain.enums.OrigemAcao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Acao extends EntidadeBase {

    @ManyToOne(optional = false)
    private Ticket ticket;

    @Column(nullable = false)
    private int numAcao;

    @Column(nullable = false)
    private boolean acaoInterna;

    private LocalDate dataAtendimento;
    private LocalTime inicioAtendimento;
    private LocalTime fimAtendimento;

    @ManyToOne(optional = false)
    private Usuario usuarioAutor;

    //para não perder a formatação, irei armazenar o própio HTML
    //Text -> sem limite de caracteres
    @Column(columnDefinition = "TEXT", nullable = false)
    private String html;

    // Texto puro, para buscas aprimoradas
    // Limite de 10k de caracteres.
    //TODO: Implementar validação no front e antes de salvar no banco
    @Column(length = 10000, nullable = false)
    private String textoPuro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrigemAcao origemAcao;

    @OneToMany(mappedBy = "acao")
    private List<Anexo> anexos;
}

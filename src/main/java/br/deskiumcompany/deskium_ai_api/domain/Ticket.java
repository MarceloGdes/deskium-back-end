package br.deskiumcompany.deskium_ai_api.domain;

import br.deskiumcompany.deskium_ai_api.domain.enums.Status;
import br.deskiumcompany.deskium_ai_api.domain.enums.SubStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket extends EntidadeBase{
    @Column(nullable = false, length = 100)
    private String titulo;

    private LocalDateTime previsaoResolucao;
    private LocalDateTime dataResolucao;
    private LocalDateTime previsaoPrimeiraResposta;
    private LocalDateTime dataPrimeiraResposta;
    private LocalTime horasApontadas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne(optional = false)
    private Solicitante solicitante;

    @ManyToOne(optional = false)
    private Suporte suporte;

    @ManyToOne()
    private Motivo motivo;

    @ManyToOne()
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubStatus subStatus;

    @ManyToOne()
    private Prioridade prioridade;

    @OneToMany(mappedBy = "ticket")
    private List<Acao> acoes;
}

package br.deskiumcompany.deskium_ai_api.dto.ticket;

import br.deskiumcompany.deskium_ai_api.domain.Categoria;
import br.deskiumcompany.deskium_ai_api.domain.Motivo;
import br.deskiumcompany.deskium_ai_api.domain.Prioridade;
import br.deskiumcompany.deskium_ai_api.domain.Ticket;
import br.deskiumcompany.deskium_ai_api.domain.enums.Status;
import br.deskiumcompany.deskium_ai_api.dto.solicitante.SolicitanteResponseDTO;
import br.deskiumcompany.deskium_ai_api.dto.suporte.SuporteResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketGetAllResponseDTO {
    private Long id;
    private LocalDateTime criadoEm;
    private String titulo;
    private LocalDateTime previsaoResolucao;
    private LocalDateTime dataResolucao;
    private LocalDateTime previsaoPrimeiraResposta;
    private LocalDateTime dataPrimeiraResposta;
    private LocalTime horasApontadas;
    private Status status;
    private SolicitanteResponseDTO solicitante;
    private SuporteResponseDTO suporte;
    private Motivo motivo;
    private Categoria categoria;
    private String subStatus;
    private Prioridade prioridade;

    public TicketGetAllResponseDTO(Ticket ticket) {
        this.id = ticket.getId();
        this.criadoEm = ticket.getCriadoEm();
        this.titulo = ticket.getTitulo();
        this.previsaoResolucao = ticket.getPrevisaoResolucao();
        this.dataResolucao = ticket.getDataResolucao();
        this.previsaoPrimeiraResposta = ticket.getPrevisaoPrimeiraResposta();
        this.dataPrimeiraResposta = ticket.getDataPrimeiraResposta();
        this.horasApontadas = ticket.getHorasApontadas();
        this.status = ticket.getStatus();
        this.solicitante = new SolicitanteResponseDTO(ticket.getSolicitante());
        this.suporte = new SuporteResponseDTO(ticket.getSuporte());
        this.motivo = ticket.getMotivo();
        this.categoria = ticket.getCategoria();
        this.subStatus = ticket.getSubStatus().getDescricao();
        this.prioridade = ticket.getPrioridade();

    }
}

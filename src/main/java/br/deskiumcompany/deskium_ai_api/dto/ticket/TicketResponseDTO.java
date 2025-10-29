package br.deskiumcompany.deskium_ai_api.dto.ticket;

import br.deskiumcompany.deskium_ai_api.domain.*;
import br.deskiumcompany.deskium_ai_api.dto.acao.AcaoResponseDTO;
import br.deskiumcompany.deskium_ai_api.dto.solicitante.SolicitanteResponseDTO;
import br.deskiumcompany.deskium_ai_api.dto.status.StatusDTO;
import br.deskiumcompany.deskium_ai_api.dto.substatus.SubStatusDTO;
import br.deskiumcompany.deskium_ai_api.dto.suporte.SuporteResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponseDTO {
    private Long id;
    private LocalDateTime criadoEm;
    private String titulo;
    private LocalDateTime previsaoResolucao;
    private LocalDateTime dataResolucao;
    private LocalDateTime previsaoPrimeiraResposta;
    private LocalDateTime dataPrimeiraResposta;
    private LocalTime horasApontadas;
    private StatusDTO status;
    private SolicitanteResponseDTO solicitante;
    private SuporteResponseDTO suporte;
    private Motivo motivo;
    private Categoria categoria;
    private SubStatusDTO subStatus;
    private Prioridade prioridade;
    private List<AcaoResponseDTO> acoes;

    public TicketResponseDTO(Ticket ticket) {
        this.id = ticket.getId();
        this.criadoEm = ticket.getCriadoEm();
        this.titulo = ticket.getTitulo();
        this.previsaoResolucao = ticket.getPrevisaoResolucao();
        this.dataResolucao = ticket.getDataResolucao();
        this.previsaoPrimeiraResposta = ticket.getPrevisaoPrimeiraResposta();
        this.dataPrimeiraResposta = ticket.getDataPrimeiraResposta();
        this.horasApontadas = ticket.getHorasApontadas();
        this.status = new StatusDTO(ticket.getStatus());
        this.solicitante = new SolicitanteResponseDTO(ticket.getSolicitante());
        this.suporte = new SuporteResponseDTO(ticket.getSuporte());
        this.motivo = ticket.getMotivo();
        this.categoria = ticket.getCategoria();
        this.subStatus = new SubStatusDTO(ticket.getSubStatus());
        this.prioridade = ticket.getPrioridade();

        this.acoes = new ArrayList<>();
        for (Acao acao : ticket.getAcoes()){
            this.acoes.add(new AcaoResponseDTO(acao));
        }

    }
}

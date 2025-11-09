package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.domain.Acao;
import br.deskiumcompany.deskium_ai_api.domain.Ticket;
import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.domain.enums.OrigemAcao;
import br.deskiumcompany.deskium_ai_api.domain.enums.Status;
import br.deskiumcompany.deskium_ai_api.domain.enums.SubStatus;
import br.deskiumcompany.deskium_ai_api.dto.acao.AcaoInsertDTO;
import br.deskiumcompany.deskium_ai_api.dto.ticket.TicketGetAllResponseDTO;
import br.deskiumcompany.deskium_ai_api.dto.ticket.TicketInsertDTO;
import br.deskiumcompany.deskium_ai_api.dto.ticket.TicketResponseDTO;
import br.deskiumcompany.deskium_ai_api.dto.ticket.TicketUpdateDTO;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.service.AcaoService;
import br.deskiumcompany.deskium_ai_api.service.TicketService;
import com.google.api.client.http.HttpStatusCodes;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("tickets")
public class TicketController {

    @Autowired
    private TicketService service;

    @Autowired
    private AcaoService acaoService;

    //O Spring injeta  o objeto Authentication na requisição, pois no meu Security Filter, a authenticação foi setada no contexto da requisição.
    //Nessa autenticação, o usuário já foi carregado previamente.
    @PostMapping
    private ResponseEntity<TicketResponseDTO> create(
            @RequestBody @Valid TicketInsertDTO dto,
            Authentication authentication,
            UriComponentsBuilder builder
    ) throws BussinesException, IOException {

        var usuario = (Usuario) authentication.getPrincipal();
        var ticket = service.insert(new Ticket(dto, usuario, OrigemAcao.SISTEMA));
        var response = new TicketResponseDTO(ticket);

        URI uri = builder.path("/tickets/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("{id}")
    private ResponseEntity<TicketResponseDTO> getById(
            Authentication auth,
            @PathVariable Long id){

        var usuario = (Usuario) auth.getPrincipal();
        Ticket ticket = service.getById(id, usuario);

        return ResponseEntity.ok(new TicketResponseDTO(ticket));
    }

    @GetMapping()
    private ResponseEntity<List<TicketGetAllResponseDTO>> getAll(
            Authentication auth,
            @RequestParam(value = "status", required = false) Status status,
            @RequestParam(value = "ticketId", required = false) Long ticketId,
            //declarando o default value, pois quando a string é nula, acaba estourando erro no banco de dados.
            @RequestParam(value = "assunto", required = false, defaultValue = "") String assunto,
            @RequestParam(value = "responsavel", required = false, defaultValue = "") String suporte,
            @RequestParam(value = "solicitante", required = false, defaultValue = "") String solicitante,
            @RequestParam(value = "subStatus", required = false) SubStatus subStatus,
            @RequestParam(value = "motivoId", required = false) Long motivoId,
            @RequestParam(value = "categoriaId", required = false) Long categoriaId,
            @RequestParam(value = "allTickets", required = true) boolean allTickets,
            @RequestParam(value = "dataAberturaInicio", required = false)LocalDateTime dataAberturaInicio,
            @RequestParam(value = "dataAberturaFim", required = false)LocalDateTime dataAberturaFim,
            @RequestParam(value = "dataFechamentoInicio", required = false)LocalDateTime dataFechamentoInicio,
            @RequestParam(value = "dataFechamentoFim", required = false)LocalDateTime dataFechamentoFim
            ) throws BussinesException {

        var usuario = (Usuario) auth.getPrincipal();
        List<Ticket> tickets = service.getAll(usuario, status, ticketId, assunto,
                suporte, solicitante, subStatus, motivoId, categoriaId, dataAberturaInicio,
                dataAberturaFim, dataFechamentoInicio, dataFechamentoFim, allTickets);

        List<TicketGetAllResponseDTO> ticketsResponse = new ArrayList<>();

        tickets.forEach(ticket -> {
            ticketsResponse.add(new TicketGetAllResponseDTO(ticket));
        });

        return ResponseEntity.ok(ticketsResponse);
    }

    @PostMapping("{id}/acoes")
    public ResponseEntity addAcao(
            @PathVariable("id") Long ticketId,
            Authentication auth,
            @RequestBody @Valid AcaoInsertDTO dto
    ) throws BussinesException {
        var usuario = (Usuario) auth.getPrincipal();
        var ticket = service.getById(ticketId, usuario);
        var acao = new Acao(ticket, usuario, dto, OrigemAcao.SISTEMA);
        acaoService.addAcao(acao);

        //Não tem URI de consulta apenas para uma Ação.
        return ResponseEntity.status(HttpStatusCodes.STATUS_CODE_CREATED).build();
    }

    @PutMapping("{id}")
    public ResponseEntity update(
            @PathVariable("id") long ticketId,
            Authentication auth,
            @RequestBody @Valid TicketUpdateDTO dto
    ) throws BussinesException {
        var usuario = (Usuario) auth.getPrincipal();
        var ticket = service.getById(ticketId, usuario);
        service.update(ticket, dto, usuario);

        return ResponseEntity.ok().build();
    }
}

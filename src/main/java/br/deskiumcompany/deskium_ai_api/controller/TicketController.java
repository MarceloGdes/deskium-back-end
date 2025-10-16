package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.domain.Ticket;
import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.dto.ticket.TicketInsertDTO;
import br.deskiumcompany.deskium_ai_api.dto.ticket.TicketResponseDTO;
import br.deskiumcompany.deskium_ai_api.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("tickets")
public class TicketController {

    @Autowired
    private TicketService service;

    //O Spring injeta  o objeto Authentication na requisição, pois no meu Security Filter, a authenticação foi setada no contexto da requisição.
    //Nessa autenticação, o usuário já foi carregado previamente.
    @PostMapping
    private ResponseEntity<TicketResponseDTO> create(@RequestBody @Valid TicketInsertDTO dto,
                                                     Authentication authentication,
                                                     UriComponentsBuilder builder){

        var usuario = (Usuario) authentication.getPrincipal();
        var ticket = service.insert(new Ticket(dto, usuario));
        var response = new TicketResponseDTO(ticket);

        URI uri = builder.path("/tickets/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);

    }
}

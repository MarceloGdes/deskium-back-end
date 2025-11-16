package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.domain.Acao;
import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.dto.ai.GeminiResponseDTO;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.service.AIService;
import br.deskiumcompany.deskium_ai_api.service.AcaoService;
import br.deskiumcompany.deskium_ai_api.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("ai")
public class AIController {
    @Autowired
    private AIService service;

    @Autowired
    private AcaoService acaoService;

    @Autowired
    private TicketService ticketService;

    @PostMapping("transcribe-audio/{ticketId}/{fileName}")
    public ResponseEntity<GeminiResponseDTO> transcribeAudio(
            @PathVariable(value = "fileName") String fileName,
            @PathVariable(value = "ticketId") Long ticketId,
            Authentication auth
    ) throws IOException, BussinesException {
        validateUser(ticketId, (Usuario) auth.getPrincipal());

        GeminiResponseDTO response = new GeminiResponseDTO(service.transcribe(fileName));
        return ResponseEntity.ok(response);
    }

    @PostMapping("generate-email/{ticketId}/{acaoId}")
    public ResponseEntity<GeminiResponseDTO> generateEmail(
            @PathVariable(value = "acaoId") Long acaoId,
            @PathVariable(value = "ticketId") Long ticketId,
            Authentication auth
    ) throws BussinesException {
        validateUser(ticketId, (Usuario) auth.getPrincipal());

        Acao acao = acaoService.getById(acaoId, ticketId);
        GeminiResponseDTO response = new GeminiResponseDTO(service.generateEmail(acao));

        return ResponseEntity.ok(response);
    }

    private void validateUser(Long ticketId, Usuario usuario) throws BussinesException {
        var ticket = ticketService.getById(ticketId, usuario);

        if(!ticket.getSuporte().getUsuario().getId().equals(usuario.getId()))
            throw new BussinesException("Usuário sem acesso alterações no ticket");
    }
}

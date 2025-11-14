package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.domain.Acao;
import br.deskiumcompany.deskium_ai_api.dto.ai.GeminiResponseDTO;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.service.AIService;
import br.deskiumcompany.deskium_ai_api.service.AcaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("ai")
public class AIController {
    @Autowired
    private AIService service;

    @Autowired
    private AcaoService acaoService;

    @PostMapping("transcribe-audio/{fileName}")
    public ResponseEntity<GeminiResponseDTO> transcribeAudio(@PathVariable(value = "fileName") String fileName) throws IOException, BussinesException {
        GeminiResponseDTO response = new GeminiResponseDTO(service.transcribe(fileName));

        return ResponseEntity.ok(response);
    }

    @PostMapping("generate-email")
    public ResponseEntity<GeminiResponseDTO> generateEmail(
            @RequestParam(value = "acaoId", required = true) Long acaoId,
            @RequestParam(value = "ticketId", required = true) Long ticketId) throws IOException, BussinesException {

        Acao acao = acaoService.getById(acaoId, ticketId);
        GeminiResponseDTO response = new GeminiResponseDTO(service.generateEmail(acao));

        return ResponseEntity.ok(response);
    }
}

package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.domain.Acao;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.service.AIService;
import br.deskiumcompany.deskium_ai_api.service.AcaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("ai")
public class AIController {
    @Autowired
    private AIService service;

    @Autowired
    private AcaoService acaoService;

    @PostMapping("transcribe-audio")
    public ResponseEntity<String> transcribeAudio(@RequestParam(value = "fileName", required = true) String fileName) throws IOException, BussinesException {
        String response = service.transcribe(fileName);

        return ResponseEntity.ok(response);
    }

    @PostMapping("generate-email")
    public ResponseEntity<String> generateEmail(
            @RequestParam(value = "acaoId", required = true) Long acaoId,
            @RequestParam(value = "ticketId", required = true) Long ticketId) throws IOException, BussinesException {

        Acao acao = acaoService.getById(acaoId, ticketId);
        String response = service.generateEmail(acao);

        return ResponseEntity.ok(response);
    }
}

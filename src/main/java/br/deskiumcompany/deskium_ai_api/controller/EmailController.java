package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/teste")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/email-personalizado")
    public ResponseEntity<String> testePersonalizado(
            @RequestParam String email,
            @RequestParam(defaultValue = "Teste") String assunto) {
        try {
            emailService.enviarEmailComAnexo(
                    email,
                    assunto,
                    "E-mail de teste enviado em: " + LocalDateTime.now(),
                    "../src/main/java/br/deskiumcompany/deskium_ai_api/infra/storage/teste.txt"
            );
            return ResponseEntity.ok("E-mail enviado para: " + email);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Erro: " + e.getMessage());
        }
    }
}
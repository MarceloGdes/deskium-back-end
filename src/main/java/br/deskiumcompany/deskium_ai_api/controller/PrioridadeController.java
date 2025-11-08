package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.domain.Prioridade;
import br.deskiumcompany.deskium_ai_api.service.PrioridadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("prioridades")
public class PrioridadeController {
    @Autowired
    private PrioridadeService service;

    @GetMapping
    private ResponseEntity<List<Prioridade>> getAll(){
        return ResponseEntity.ok(service.getAll());
    }
}

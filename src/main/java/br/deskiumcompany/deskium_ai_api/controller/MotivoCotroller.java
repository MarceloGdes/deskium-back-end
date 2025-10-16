package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.domain.Motivo;
import br.deskiumcompany.deskium_ai_api.service.MotivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("motivos")
public class MotivoCotroller {

    @Autowired
    private MotivoService service;

    @GetMapping
    public ResponseEntity<List<Motivo>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}

package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.domain.Suporte;
import br.deskiumcompany.deskium_ai_api.dto.suporte.SuporteInsertDTO;
import br.deskiumcompany.deskium_ai_api.dto.suporte.SuporteResponseDTO;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.service.SuporteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("suportes")
public class SuporteController {

    @Autowired
    private SuporteService service;

    @PostMapping
    public ResponseEntity<SuporteResponseDTO> create(
            @RequestBody @Valid SuporteInsertDTO dto, UriComponentsBuilder builder) throws BussinesException {

        Suporte suporte = new Suporte(dto);
        suporte = service.insert(suporte);

        SuporteResponseDTO response = new SuporteResponseDTO(suporte);

        URI uri = builder.path("/suportes/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuporteResponseDTO> getById(@PathVariable Long id) throws EntityNotFoundException {
        Suporte suporte = service.getById(id);
        return ResponseEntity.ok(new SuporteResponseDTO(suporte));
    }
}

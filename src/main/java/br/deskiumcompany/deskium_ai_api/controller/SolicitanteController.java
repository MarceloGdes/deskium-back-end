package br.deskiumcompany.deskium_ai_api.controller;


import br.deskiumcompany.deskium_ai_api.domain.Empresa;
import br.deskiumcompany.deskium_ai_api.domain.Solicitante;
import br.deskiumcompany.deskium_ai_api.dto.solicitante.SolicitanteInsertDTO;
import br.deskiumcompany.deskium_ai_api.dto.solicitante.SolicitanteResponseDTO;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.service.SolicitanteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("solicitantes")
public class SolicitanteController {

    @Autowired
    private SolicitanteService solicitanteService;
    @PostMapping
    public ResponseEntity<SolicitanteResponseDTO> create(
            @RequestBody @Valid SolicitanteInsertDTO dto, UriComponentsBuilder builder) throws BussinesException {

        Solicitante solicitante = new Solicitante(dto);
        solicitante = solicitanteService.insert(solicitante);

        SolicitanteResponseDTO response = new SolicitanteResponseDTO(solicitante);

        URI uri = builder.path("/empresas/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }
}

package br.deskiumcompany.deskium_ai_api.controller;


import br.deskiumcompany.deskium_ai_api.domain.Solicitante;
import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.dto.solicitante.SolicitanteInsertDTO;
import br.deskiumcompany.deskium_ai_api.dto.solicitante.SolicitanteResponseDTO;
import br.deskiumcompany.deskium_ai_api.dto.solicitante.SolicitanteUpdateDto;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.service.SolicitanteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
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

        URI uri = builder.path("/solicitantes/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    //Authenticação injetada no contexto pelo SpringSecurity
    @GetMapping("/me")
    public ResponseEntity<SolicitanteResponseDTO> getByLoggedUser(Authentication auth) throws EntityNotFoundException {
        Usuario usuario = (Usuario) auth.getPrincipal();
        Solicitante solicitante = solicitanteService.getByUsuarioId(usuario.getId());
        return ResponseEntity.ok(new SolicitanteResponseDTO(solicitante));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitanteResponseDTO> getById(@PathVariable Long id) throws EntityNotFoundException {
        Solicitante solicitante = solicitanteService.getById(id);
        return ResponseEntity.ok(new SolicitanteResponseDTO(solicitante));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitanteResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid SolicitanteUpdateDto dto) throws EntityNotFoundException, BussinesException {

        Solicitante solicitante = new Solicitante(dto);
        solicitante = solicitanteService.update(id, solicitante);

        return ResponseEntity.ok(new SolicitanteResponseDTO(solicitante));
    }
}

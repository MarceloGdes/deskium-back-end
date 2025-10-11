package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.domain.Empresa;
import br.deskiumcompany.deskium_ai_api.dto.empresa.EmpresaDTO;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.service.EmpresaService;
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
@RequestMapping("empresas")
public class EmpresaController {

    @Autowired
    private  EmpresaService empresaService;
    @PostMapping
    public ResponseEntity<Empresa> create(
            @RequestBody @Valid EmpresaDTO dto, UriComponentsBuilder builder) throws BussinesException {

        Empresa empresa = new  Empresa(dto);
        empresa = empresaService.insert(empresa);

        URI uri = builder.path("/empresas/{id}").buildAndExpand(empresa.getId()).toUri();
        return ResponseEntity.created(uri).body(empresa);
    }
}

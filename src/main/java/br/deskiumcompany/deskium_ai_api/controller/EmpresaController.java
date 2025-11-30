package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.domain.Empresa;
import br.deskiumcompany.deskium_ai_api.dto.empresa.EmpresaDTO;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<Empresa>> findAll(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String razaoSocial,
            @RequestParam(required = false) String cnpj) {
        List<Empresa> empresas = empresaService.findAll(id, razaoSocial, cnpj);
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> findById(@PathVariable Long id) {
        Empresa empresa = empresaService.getById(id);
        return ResponseEntity.ok(empresa);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(
            @PathVariable Long id,
            @RequestBody @Valid EmpresaDTO dto) throws BussinesException {

        empresaService.update(id, new Empresa(dto));
        return ResponseEntity.ok().build();
    }
}

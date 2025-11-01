package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.dto.arquivo.ArquivoDTO;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.service.ArquivosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("arquivos")
public class ArquivoController {

    @Autowired
    private ArquivosService service;

    //MultipartFile é uma interface usada para representar um arquivo enviado via upload (multipart/form-data) em uma requisição HTTP
    //Spring recebe esse arquivo e faz o gerenciamento.
    @PostMapping()
    public ResponseEntity upload(
            @RequestParam("files") List<MultipartFile> files,
            UriComponentsBuilder builder
    ) throws IOException, BussinesException {
        var fileNames = service.saveFiles(files);

        List<ArquivoDTO> arquivosDTO = new ArrayList<>();
        fileNames.forEach(fileName -> {
            arquivosDTO.add(new ArquivoDTO(fileName));
        });

        URI uri = builder.path("/arquivos/fileName").buildAndExpand().toUri();
        return ResponseEntity.created(uri).body(arquivosDTO);
    }
}

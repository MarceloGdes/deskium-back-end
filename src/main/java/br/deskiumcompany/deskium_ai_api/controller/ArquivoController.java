package br.deskiumcompany.deskium_ai_api.controller;

import br.deskiumcompany.deskium_ai_api.dto.arquivo.ArquivoDTO;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.service.ArquivosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
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

    @GetMapping("{fileName}")
    public ResponseEntity<Resource> getByFileName(@PathVariable("fileName") String fileName) throws IOException {
        File file = service.getFileByFileName(fileName);

        //Gera um recurso com base no arquivo;
        //representa qualquer coisa que possa ser lida como um arquivo
        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                //Resgata o MediaType do arquivo para enviar no header da resposta e não corromper o arquivo
                .contentType(MediaType.parseMediaType(Files.probeContentType(file.toPath())))
                .body(resource);
    }

    //Quando der algum erro ou exceção no momento de salvar o ticket e ações, precisamos deletar o que previamente
    // foi armazenado
    @DeleteMapping("{fileName}")
    public ResponseEntity removeByFileNames(@PathVariable("fileName") String fileName) throws IOException {
        service.deleteFileByName(fileName);
        return ResponseEntity.ok().build();
    }
}

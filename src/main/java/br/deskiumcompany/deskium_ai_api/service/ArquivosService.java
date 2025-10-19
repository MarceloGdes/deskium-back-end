package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ArquivosService {
    @Value("${upload.dir}")
    private String uploadDir;

    public String saveFile(MultipartFile file) throws BussinesException, IOException {
        if(file.isEmpty())
            throw new BussinesException("Nenhum arquivo enviado");


        Path uploadsPath = Paths.get(uploadDir);

        if(!Files.exists(uploadsPath)){
            Files.createDirectories(uploadsPath);
        }

        //Criando filePath de destiono e copiando o arquivo.
        String fileName = UUID.randomUUID() + "_" +  file.getOriginalFilename()
                .replace(" ", "-");
        Path filePath = uploadsPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    public boolean existsByFileName(String fileName){
        return Files.exists(Paths.get(uploadDir, fileName));
    }
}

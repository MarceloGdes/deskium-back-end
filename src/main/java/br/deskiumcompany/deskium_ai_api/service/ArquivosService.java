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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ArquivosService {
    @Value("${upload.dir}")
    private String uploadDir;

    public List<String> saveFiles(List<MultipartFile> files) throws BussinesException, IOException {
        Path uploadsPath = Paths.get(uploadDir);
        ArrayList<String> fileNames = new ArrayList<>();

        if(!Files.exists(uploadsPath)){
            Files.createDirectories(uploadsPath);
        }

        for(MultipartFile file : files){
            if(file.isEmpty())
                throw new BussinesException("Um dos arquivos não foi enviado corretamente. Verifique.");
        }

        for(MultipartFile file : files){
            //Criando filePath de destiono e copiando o arquivo.
            String fileName = UUID.randomUUID() + "_" +  file.getOriginalFilename()
                    .replace(" ", "-");
            Path filePath = uploadsPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            fileNames.add(fileName);
        }

        return fileNames;
    }

    //Quando der algum erro ou exceção no momento de salvar o ticket e ações, precisamos deletar o que previamente
    // foi armazenado
    public void deleteFileByName(String fileName) throws IOException {
        Path path = Paths.get(uploadDir);
        Files.deleteIfExists(path.resolve(fileName));
    }

    public boolean existsByFileName(String fileName){
        return Files.exists(Paths.get(uploadDir, fileName));
    }
}

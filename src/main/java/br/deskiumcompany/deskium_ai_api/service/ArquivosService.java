package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ArquivosService {
    @Value("${upload.dir}")
    private String uploadDir;

    private static List<String> TIPOS_PERMITIDOS = Arrays.asList(
            "application/pdf",
            "image/jpeg",
            "image/png",
            "audio/mpeg",
            "audio/wav",
            "audio/x-wav",
            "audio/mp4",
            "audio/x-m4a",
            "audio/ogg"
    );

    public List<String> saveFiles(List<MultipartFile> files) throws BussinesException, IOException {
        Path uploadsPath = Paths.get(uploadDir);
        ArrayList<String> fileNames = new ArrayList<>();

        if(!Files.exists(uploadsPath)){
            Files.createDirectories(uploadsPath);
        }

        for(MultipartFile file : files){
            if(file == null || file.isEmpty())
                throw new BussinesException("Um dos arquivos não foi enviado corretamente. Verifique.");

            var contentType = file.getContentType();
            if(!TIPOS_PERMITIDOS.contains(contentType)){
                throw new BussinesException("Arquivo " + file.getOriginalFilename() + " não é um tipo permitido");
            }
        }

        for(MultipartFile file : files){
            //Criando filePath de destione e copiando o arquivo.
            String fileName = UUID.randomUUID() + "_" +  file.getOriginalFilename()
                    .replace(" ", "-");
            Path filePath = uploadsPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            fileNames.add(fileName);
        }

        return fileNames;
    }

    public void deleteFileByName(String fileName) throws IOException {
        Path path = Paths.get(uploadDir);
        Files.deleteIfExists(path.resolve(fileName));
    }

    protected boolean existsByFileName(String fileName){
        return Files.exists(Paths.get(uploadDir, fileName));
    }

    public File getFileByFileName(String fileName) throws FileNotFoundException {
        if(!existsByFileName(fileName))
            throw new FileNotFoundException("Arquivo " + fileName + " não encontrado");

        return new File(Paths.get(uploadDir, fileName).toUri());
    }
}

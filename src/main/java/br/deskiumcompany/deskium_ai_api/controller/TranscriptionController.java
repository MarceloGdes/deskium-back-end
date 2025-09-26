package br.deskiumcompany.deskium_ai_api.controller;
import com.google.genai.Client;
import com.google.genai.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.IOException;

import java.util.List;

@RestController
@RequestMapping("/api/transcribe")
public class TranscriptionController {

    //private final VertexAiGeminiChatModel geminiChatModel;
//
//    @Autowired
//    public TranscriptionController(VertexAiGeminiChatModel geminiChatModel) {
//        this.geminiChatModel = geminiChatModel;
//    }

    @Value("${google.api.key}")
    private String apiKey;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> transcribeAudio(@RequestParam("file") MultipartFile file) throws IOException {

        Client client = Client.builder()
                .apiKey(apiKey)
                .build();

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhum arquivo de áudio enviado.");
        }

        // Converte o MultipartFile em um Resource do Spring
        Resource audioResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };

        File uploadedFile = client.files.upload(audioResource.getContentAsByteArray(), UploadFileConfig.builder()
                .mimeType(file.getContentType())
                .name(file.getName())
                .build());

        List<Part> parts = List.of(
                Part.fromText("Trancreva esse audio referente a uma gravação telefonica realizada entre um atendente " +
                        "e o cliente. "),
                Part.fromUri(uploadedFile.uri().get(), uploadedFile.mimeType().get())
        );

        var response = client.models.generateContent(
                "gemini-2.5-flash",
                Content.builder()
                        .parts(parts)
                        .build(),
                null);





//        // Cria a mensagem multimodal para a Gemini API
//        UserMessage userMessage = UserMessage.builder()
//                .text("Transcreva o áudio abaixo em português.")
//                .media(List.of(new Media(MediaType.parseMediaType(file.getContentType()), audioResource)))
//                .build();
//
//
//        // 2. Encapsule a mensagem em um objeto Prompt
//        Prompt prompt = new Prompt(userMessage);
//
//        // 3. Passe o objeto Prompt para o método call
//        var response = geminiChatModel.call(prompt);
//
//        // 4. Extraia o conteúdo de texto da resposta
//        String transcription = response.getResult().getOutput().getText();
//
        return ResponseEntity.ok(response.text());
    }

    @GetMapping("/text")
    public ResponseEntity<String> generateTextFromTextInput(){
        Client client = Client.builder()
                .apiKey(apiKey)
                .build();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",
                        "Explain how AI works in a few words",
                        null);

        return ResponseEntity.ok(response.text());
    }
}

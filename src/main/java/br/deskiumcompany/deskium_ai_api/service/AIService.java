package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Acao;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import com.google.genai.Client;
import com.google.genai.types.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

@Service
public class AIService {
    @Value("${google.api.key}")
    private String apiKey;

    @Autowired
    private ArquivosService arquivosService;

    private static List<String> TIPOS_AUDIOS_PERMITIDOS = Arrays.asList(
            "audio/mpeg",
            "audio/wav",
            "audio/x-wav",
            "audio/mp4",
            "audio/x-m4a",
            "audio/ogg"
    );

    private static String PROMPT_TRANSCRICAO = """
            Trancreva o audio enviado. Ele se refere a uma ligação telefonica.
            
            Regras de formatação:
            - Separe cada fala em um parágrafo <p>.
            - Identifique as pessoas da ligação e coloque o nome da pessoa entre a <strong>...</strong>, em cada respectiva fala.
            - Mantenha as marcações de tempo (se houver) no formato [mm:ss] dentro do parágrafo, no começo de cada linha.
            - Retorne apenas o HTML, sem comentários, emojis, explicações, formatações de markdown ou formatações adicionais. Texto plano com as devidas tags citadas.
                       
            Exemplo:
            <p><strong>Nome do atendente:</strong> Olá, bom dia!</p>
            <p><strong>Nome do Cliente:</strong> Bom dia, tudo bem?</p>
            """;

    private static String PROMPT_GERACAO_EMAIL = """
            Com base na transcrição abaixo, redija o corpo de um e-mail formalizando o atendimento.
            Regras obrigatórias:
            Utilize linguagem formal e normas do português do Brasil (PT-BR).         
            Não use emojis.      
            Retorne APENAS o conteúdo HTML do corpo do e-mail — ou seja, somente tags <p> com texto dentro. Não inclua <html>, <head>, <body>, nem metadados, nem explicações.  
            Cada parágrafo do e-mail deve estar em sua própria tag <p>...</p>.        
            Use parágrafos claros: abertura (saudação e identificação do atendimento), resumo objetivo do diálogo (quem falou, assunto), e conclusão com próximos passos ou orientação ao cliente.
            Não invente informações além do que consta na transcrição. Caso algo seja incerto, registre como “esclarecimento solicitado ao cliente” ou similar.  
            Não inclua assinaturas automáticas, apenas o corpo formal do e-mail.    
                        
            Resposta esperada: somente HTML com parágrafos <p>...</p>. Exemplos de conteúdo aceitáveis (mas não incluir texto explicativo):   
            <p>Prezado Senhor [NOME CLIENTE], ...</p>
            <p>Conforme nosso contato por telefone: ...</p>
            
            Transcricao abaixo:
                        
            """;

    public String transcribe(String audioFileName) throws IOException, BussinesException {
        Client client = Client.builder()
                .apiKey(apiKey)
                .build();

        var file = arquivosService.getFileByFileName(audioFileName);
        //Identifica o mimeType do arquivo.
        var mimeType = Files.probeContentType(file.toPath());

        if (mimeType == null || !TIPOS_AUDIOS_PERMITIDOS.contains(mimeType)) {
            throw new BussinesException("O arquivo enviado não é um tipo de áudio permitido.");
        }

        // Converte o File em uma Resource do Spring
        Resource audioResource = new FileSystemResource(file);

        //Faz o upload do audio para os servidores do google
        File uploadedFile = client.files.upload(audioResource.getContentAsByteArray(), UploadFileConfig.builder()
                .mimeType(mimeType)
                .name(file.getName().split("_")[0]) //Remove o nome depois do UUID, pois o gemini tem limite de 40 caracteres para nome de arquivos.
                .build());

        List<Part> parts = List.of(
                Part.fromText(PROMPT_TRANSCRICAO),
                Part.fromUri(uploadedFile.uri().get(), uploadedFile.mimeType().get())
        );

        GenerateContentResponse response = client.models.generateContent(
                "gemini-2.5-flash",
                Content.builder()
                        .parts(parts)
                        .build(),
                null);

        return response.text();
    }

    public String generateEmail(Acao acao) throws BussinesException {
        Client client = Client.builder()
                .apiKey(apiKey)
                .build();

        if(!acao.isAcaoTranscricao())
            throw new BussinesException("A ação não é uma transcrição.");

        var textoAcao = acao.getTextoPuro();
        if(textoAcao.isBlank())
            throw new BussinesException("O texto da ação não foi preenchido.");

        List<Part> parts = List.of(
                Part.fromText(PROMPT_GERACAO_EMAIL),
                Part.fromText(textoAcao)
        );

        GenerateContentResponse response = client.models.generateContent(
                "gemini-2.5-flash",
                Content.builder()
                        .parts(parts)
                        .build(),
                null);

        return response.text();
    }
}

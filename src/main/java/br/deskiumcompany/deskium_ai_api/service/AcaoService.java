package br.deskiumcompany.deskium_ai_api.service;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcaoService {
    @Autowired
    private ArquivosService arquivosService;

    //TODO: utilizar o clean do Jsoup aumentando a securaça contra ataques XSS (tags html que executam scripts)

    public boolean arquivoExistsByFileName(String fileName){
        return arquivosService.existsByFileName(fileName);
    }

    public String extractTextFromHTML(String html){
        var text = Jsoup.parse(html).text();
        return text;
    }
}

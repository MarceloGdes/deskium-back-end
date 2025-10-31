package br.deskiumcompany.deskium_ai_api.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

    protected String extractTextFromHTML(String html){
        var text = Jsoup.parse(html).text();
        return text;
    }

    protected String formatHtml(String html) {
        // Com Jsoup, percorremos o HTML em busca de tags img para adicionar a classe img-fluid do boostrap
        Document doc = Jsoup.parse(html);

        for (Element img : doc.select("img")){
            img.addClass("img-fluid");
        }

        return doc.html();
    }
}

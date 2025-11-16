package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Anexo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private ArquivosService arquivosService;

    //Envia e-mails com anexo e em formato HTML
    @Async
    //Metodo assincrono para não demorar para devolver a resposta para o client.
    //Não ou enviar o corpo do e-mail nesse primeiro momento, devido as imagens não serem carregadas.
    public void enviarEmailComAnexo(String destinatario,
                                    String assunto,
                                    String conteudoHtmlAcao,
                                    List<Anexo> anexos) throws MessagingException, FileNotFoundException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("guedesmarcello733@gmail.com");
        helper.setTo(destinatario);
        helper.setSubject(assunto);
        helper.setText(gerarHTMLEmail(conteudoHtmlAcao), true);

        if (anexos != null && !anexos.isEmpty()) {
            for (Anexo anexo : anexos) {
                File file = arquivosService.getFileByFileName(anexo.getFileName());
                FileSystemResource fileSystemResource = new FileSystemResource(file);
                helper.addAttachment(fileSystemResource.getFilename(), fileSystemResource);
            }
        }

        emailSender.send(message);

    }

    public String gerarHTMLEmail(String conteudoTicket) {
        String template = """
        <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background-color: #ffffff;">
            <h3 style="color: #333; font-size: 20px; margin-bottom: 20px;">Seu ticket foi atualizado!</h3>
            <p style="color: #666; font-size: 14px; font-style: italic; margin-bottom: 20px;">Imagens podem não carregar, acesse o Deskium para visualizar e responder o ticket.</p>
            <div id="conteudo" style="background-color: #f8f9fa; padding: 20px; border-radius: 6px; border: 1px solid #dee2e6;"></div>
            <p style="color: #666; font-size: 14px; font-style: italic; margin-bottom: 20px;">Não responda esse e-mail.</p>
        </div>
        """;

        Document doc = Jsoup.parse(template);
        doc.select("#conteudo").append(conteudoTicket);

        return doc.body().html();
    }
}

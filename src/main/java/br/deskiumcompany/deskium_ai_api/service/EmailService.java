package br.deskiumcompany.deskium_ai_api.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    //Envia e-mails com anexo e em formato HTML
    public void enviarEmailComAnexo(
            String destinatario, String assunto, String corpoHTML, String anexo) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("guedesmarcello733@gmail.com");
        helper.setTo(destinatario);
        helper.setSubject(assunto);
        helper.setText(corpoHTML);

//        FileSystemResource file
//                = new FileSystemResource(new File(anexo));
//        helper.addAttachment(file.getFilename(), file);

        emailSender.send(message);

    }
}

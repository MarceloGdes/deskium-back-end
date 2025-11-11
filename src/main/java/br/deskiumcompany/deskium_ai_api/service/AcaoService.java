package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Acao;
import br.deskiumcompany.deskium_ai_api.domain.Anexo;
import br.deskiumcompany.deskium_ai_api.domain.enums.Status;
import br.deskiumcompany.deskium_ai_api.domain.enums.SubStatus;
import br.deskiumcompany.deskium_ai_api.domain.enums.TipoUsuario;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.respository.AcaoRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcaoService {
    @Autowired
    private ArquivosService arquivosService;

    @Autowired
    private AcaoRepository repository;

    //TODO: utilizar o clean do Jsoup aumentando a segurança contra ataques XSS (tags html que executam scripts)

    public void addAcao(Acao acao, Status newStatus) throws BussinesException {
        if(!acao.getTicket().getStatus().equals(Status.ABERTO))
            throw new BussinesException("O ticket " + acao.getTicket().getId() + " não está aberto.");

        if(acao.getUsuarioAutor().getId() != acao.getTicket().getSolicitante().getUsuario().getId() &&
                acao.getUsuarioAutor().getId() != acao.getTicket().getSuporte().getUsuario().getId())
            throw new BussinesException("Usuário sem acesso a adições de ações nesse ticket");

        acao.setNumAcao(repository.findLastNumAcao(acao.getTicket()) + 1);
        acao.setTextoPuro(extractTextFromHTML(acao.getHtml()));
        acao.setHtml(formatHtml(acao.getHtml()));

        if(acao.getTextoPuro().length() > 10000)
            throw new BussinesException("A descrição da ação, ultrapassa a quantidade de 10.000 caracteres");

        if(acao.getAnexos() != null && !acao.getAnexos().isEmpty()){
            for (Anexo anexo : acao.getAnexos()) {
                if (arquivosService.existsByFileName(anexo.getFileName())) {
                    anexo.setAcao(acao);
                } else {
                    throw new BussinesException("o arquivo: " + anexo.getFileName() + " não existe. Faça upload novamente.");
                }
            }
        }

        //Atualizando o substatus do ticket.
        if(acao.getUsuarioAutor().getTipoUsuario().equals(TipoUsuario.SOLICITANTE)
                && acao.getTicket().getSubStatus().equals(SubStatus.AGUARDANDO_RETORNO)){
            acao.getTicket().setSubStatus(SubStatus.EM_ATENDIMENTO);
        }

        //Atualizando status do ticket.
        if(acao.getTicket().getSuporte().getUsuario().getId().equals(acao.getUsuarioAutor().getId()) && !acao.isAcaoInterna()){

            //Vlidação de categoria e prioridade preenchidos.
            if(acao.getTicket().getCategoria() == null)
                throw new BussinesException("Para fechar o ticket, é necessário o preenchimento da categoria.");

            if(acao.getTicket().getPrioridade() == null)
                throw new BussinesException("Para fechar o ticket, é necessário o preenchimento da prioridade.");

            acao.getTicket().setStatus(newStatus);
            acao.getTicket().setSubStatus(SubStatus.FECHADO);

        }else if(!acao.getTicket().getStatus().name().equals(newStatus.name())) {
            throw new BussinesException("Você não tem acesso para alterar o status do ticket, " +
                    "ou está alterando o status com uma ação interna.");
        }

        repository.save(acao);
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

        return doc.body().html();
    }
}

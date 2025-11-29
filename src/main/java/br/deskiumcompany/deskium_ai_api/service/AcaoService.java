package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Acao;
import br.deskiumcompany.deskium_ai_api.domain.Anexo;
import br.deskiumcompany.deskium_ai_api.domain.Ticket;
import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.domain.enums.Status;
import br.deskiumcompany.deskium_ai_api.domain.enums.SubStatus;
import br.deskiumcompany.deskium_ai_api.domain.enums.TipoUsuario;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.respository.AcaoRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class AcaoService {
    @Autowired
    private ArquivosService arquivosService;

    @Autowired
    private AcaoRepository repository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CalculadoraPrazoTicketService calculadoraPrazoTicketService;

    //TODO: utilizar o clean do Jsoup aumentando a segurança contra ataques XSS (tags html que executam scripts)

    public void addAcao(Acao acao, Status newStatus, boolean isReopenning) throws BussinesException, MessagingException, FileNotFoundException {
        var ticket = acao.getTicket();
        var usuarioSolicitante = ticket.getSolicitante().getUsuario();
        var usuarioSuporte = ticket.getSuporte().getUsuario();

        if(acao.getUsuarioAutor().getId() != usuarioSolicitante.getId() &&
                acao.getUsuarioAutor().getId() != usuarioSuporte.getId())
            throw new BussinesException("Usuário sem acesso a adições de ações nesse ticket");

        if (isReopenning) {
            reopenTicket(acao, ticket, usuarioSolicitante);

        } else if (!ticket.getStatus().equals(Status.ABERTO))
            throw new BussinesException("O ticket " + ticket.getId() + " não está aberto.");

        acao.setNumAcao(repository.findLastNumAcao(ticket) + 1);
        acao.setTextoPuro(extractTextFromHTML(acao.getHtml()));
        acao.setHtml(formatHtml(acao.getHtml()));

        if(acao.getTextoPuro().length() > 10000)
            throw new BussinesException("A descrição da ação, ultrapassa a quantidade de 10.000 caracteres.");

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
                && ticket.getSubStatus().equals(SubStatus.AGUARDANDO_RETORNO)){
            ticket.setSubStatus(SubStatus.EM_ATENDIMENTO);
        }

        //Ações realizadas pelo suporte
        if(usuarioSuporte.getId().equals(acao.getUsuarioAutor().getId())) {
            //Setando data da primeira resposta.
            if(ticket.getDataPrimeiraResposta() == null
                    && !acao.isAcaoInterna()){

                ticket.setDataPrimeiraResposta(LocalDateTime.now());
            }

            //Calculando horas apontada
            if(ticket.getHorasApontadas() != null){
                ticket.setHorasApontadas(ticket.getHorasApontadas().plus(calcularHoras(acao)));
            }else {
                ticket.setHorasApontadas(calcularHoras(acao));
            }

            //Atualizando status do ticket.
            if(!ticket.getStatus().name().equals(newStatus.name())){
                closeTicket(acao, newStatus, ticket);
            }
        }

        repository.save(acao);

        if(acao.getUsuarioAutor().getTipoUsuario().name().equals(TipoUsuario.SUPORTE.name())
                && !acao.isAcaoInterna()){

            emailService.enviarEmailComAnexo(
                    usuarioSolicitante.getEmail(),
                    "Seu ticket #" + ticket.getId() + " - '" + ticket.getTitulo() + "' foi atualizado.",
                    acao.getHtml(),
                    acao.getAnexos());
        }
    }

    public Acao getById(Long acaoId, Long ticketId) {
        return repository.findByIdAndTicketId(acaoId, ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Acão não encontrada."));
    }
    private static void closeTicket(Acao acao, Status newStatus, Ticket ticket) throws BussinesException {
        //Valida se a ação é interna.
        if(acao.isAcaoInterna())
            throw new BussinesException("O ticket não pode ser fechado com uma ação interna.");

        //Validação de categoria e prioridade preenchidos.
        if(ticket.getCategoria() == null)
            throw new BussinesException("Para fechar o ticket, é necessário o preenchimento da categoria.");

        if(ticket.getCategoria() == null)
            throw new BussinesException("Para fechar o ticket, é necessário o preenchimento da categoria.");

        if(ticket.getPrioridade() == null)
            throw new BussinesException("Para fechar o ticket, é necessário o preenchimento da prioridade.");

        ticket.setStatus(newStatus);
        ticket.setSubStatus(SubStatus.FECHADO);
        ticket.setDataResolucao(LocalDateTime.now());
    }
    private void reopenTicket(Acao acao, Ticket ticket, Usuario usuarioSolicitante) throws BussinesException {
        if (ticket.getStatus().equals(Status.CANCELADO))
            throw new BussinesException("Tickets Cancelados não podem ser reabertos.");

        // Apenas o solicitante pode reabrir
        if (!acao.getUsuarioAutor().getId().equals(usuarioSolicitante.getId()))
            throw new BussinesException("Apenas o solicitante pode reabrir o ticket.");

        // Calcula o prazo de reabertura, conforme a jornada de trabalho configurada.
        LocalDateTime prazoDeReabertura = calculadoraPrazoTicketService.somaDiasUteisTrabalho(ticket.getDataResolucao(), 2);
        if (LocalDateTime.now().isAfter(prazoDeReabertura))
            throw new BussinesException("Este ticket não pode mais ser reaberto, pois passou do prazo de 2 dias úteis");

        ticket.setDataResolucao(null);
        ticket.setStatus(Status.ABERTO);
    }
    private Duration calcularHoras(Acao acao) throws BussinesException {
        LocalDate dataAtendimento = acao.getDataAtendimento();
        LocalTime inicioAtendimento = acao.getInicioAtendimento();
        LocalTime fimAtendimento = acao.getFimAtendimento();

        if(dataAtendimento == null || inicioAtendimento == null || fimAtendimento == null)
            throw new BussinesException("Data e horários de atendimento não preenchidos. Valide se todos os campos estão preenchidos.");

        if(dataAtendimento.isBefore(acao.getTicket().getCriadoEm().toLocalDate()))
            throw new BussinesException("A data de atendimento deve ser depois da data de abertura do ticket.");

        if(inicioAtendimento.isAfter(fimAtendimento))
            throw new BussinesException("O inicio do atendimento deve ser antes do final do atendimento.");

        return Duration.between(inicioAtendimento, fimAtendimento);
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

package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Anexo;
import br.deskiumcompany.deskium_ai_api.domain.Ticket;
import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.domain.enums.Status;
import br.deskiumcompany.deskium_ai_api.domain.enums.SubStatus;
import br.deskiumcompany.deskium_ai_api.domain.enums.TipoUsuario;
import br.deskiumcompany.deskium_ai_api.dto.ticket.TicketUpdateDTO;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.respository.TicketRespository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private SolicitanteService solicitanteService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private MotivoService motivoService;

    @Autowired
    private PrioridadeService prioridadeService;

    @Autowired
    private SuporteService suporteService;

    @Autowired
    private TicketRespository respository;

    @Autowired
    private AcaoService acaoService;

    @Autowired
    private ArquivosService arquivosService;

    @Autowired
    private CalculadoraPrazoTicketService calculadoraPrazoTicketService;

    public Ticket insert(Ticket ticket) throws BussinesException{
        var solicitante = ticket.getSolicitante();
        solicitante = solicitanteService.getByUsuarioId(solicitante.getUsuario().getId());
        ticket.setSolicitante(solicitante);

        var categoria = ticket.getCategoria();
        if(categoria != null){
            categoria = categoriaService.getById(categoria.getId());
            ticket.setCategoria(categoria);
        }

        var motivo = ticket.getMotivo();
        motivo = motivoService.getById(motivo.getId());
        ticket.setMotivo(motivo);

        ticket.setSuporte(suporteService.getSuporteComMenosTickets());

        var acao = ticket.getAcoes().getFirst();
        acao.setTextoPuro(acaoService.extractTextFromHTML(acao.getHtml()));
        acao.setHtml(acaoService.formatHtml(acao.getHtml()));

        if(acao.getTextoPuro().length() > 10000)
            throw new BussinesException("A descrição ultrapassa a quantidade de 10.000 caracteres.");

        acao.setTicket(ticket);

        if(acao.getAnexos() != null && !acao.getAnexos().isEmpty()){
            for (Anexo anexo : acao.getAnexos()) {
                if (arquivosService.existsByFileName(anexo.getFileName())) {
                    anexo.setAcao(acao);
                } else {
                    throw new BussinesException("o arquivo: " + anexo.getFileName() + " não existe. Faça upload novamente.");
                }
            }
        }

        calculadoraPrazoTicketService.calcularPrazosPrimeiraRespostaEResolucao(ticket);

        return respository.save(ticket);
    }

    public List<Ticket> getAll(
            Usuario usuario, Status status, Long ticketId,
            String assunto, String suporte, String solicitante, SubStatus subStatus,
            Long motivoId, Long categoriaId, LocalDateTime dataAberturaInicio,
            LocalDateTime dataAberturaFim, LocalDateTime dataFechamentoInicio,
            LocalDateTime dataFechamentoFim, boolean allTickets

    ) throws BussinesException {
        validateDatas(dataAberturaInicio, dataAberturaFim, "data de abertura");
        validateDatas(dataFechamentoInicio, dataFechamentoFim, "data de fechamento");

        //Se for solicitante, retorna apenas os tickets dele.
        if(usuario.getTipoUsuario().equals(TipoUsuario.SOLICITANTE)){
            var tickets = respository.findAll(usuario, status, ticketId, assunto, suporte,
                    subStatus, motivoId, categoriaId, "", dataAberturaInicio,
                    dataAberturaFim, dataFechamentoInicio, dataFechamentoFim);

            tickets.forEach(ticket -> ticket.setPrioridade(null));
            return tickets;

        //Se não for, ele é suporte. Caso a flag allTickets seja true, retorna todos os tickets de todos os usuários.
        }else if(allTickets){
            return respository.findAll(null, status, ticketId, assunto, suporte,
                    subStatus, motivoId, categoriaId, solicitante, dataAberturaInicio, dataAberturaFim, dataFechamentoInicio, dataFechamentoFim);

        //se for falso, retorno os tickets do suporte
        }else {
            return respository.findAll(usuario, status, ticketId, assunto, "",
                    subStatus, motivoId, categoriaId, solicitante, dataAberturaInicio, dataAberturaFim, dataFechamentoInicio, dataFechamentoFim);
        }
    }

    public Ticket getById(Long id, Usuario usuario){
        //Valida se o solicitante pode requisitar o ticket do ID informado.
        if(usuario.getTipoUsuario().equals(TipoUsuario.SOLICITANTE) ){
            var ticket = respository.findByIdAndSolicitanteUsuarioId(id, usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Ticket não encontrado."));

            //Não será devolvido ações internas ao Solicitante.
            var acoesFiltradas = ticket.getAcoes().stream().filter(acao -> !acao.isAcaoInterna()).toList();
            ticket.setAcoes(acoesFiltradas);

            return ticket;
        }else {
            return respository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Ticket não encontrado."));
        }
    }
    private void validateDatas(LocalDateTime dataInicial, LocalDateTime dataFinal, String dataDescricao) throws BussinesException {
        if (dataInicial != null || dataFinal != null){
            if(dataInicial == null || dataFinal == null)
                throw new BussinesException("Você deve preencher o período inicial e final da " + dataDescricao +", ou deixar " +
                        "os dois campos em branco.");

            if(dataInicial.isAfter(dataFinal)){
                throw new BussinesException("A " + dataDescricao + " final deve ser igual ou posterior a " + dataDescricao + " inicial.");
            }
        }
    }
    public void update(Ticket ticket, TicketUpdateDTO dto, Usuario usuario) throws BussinesException {
        if(!ticket.getStatus().equals(Status.ABERTO))
            throw new BussinesException("O ticket " + ticket.getId() + " não está aberto.");

        if(usuario.getId() != ticket.getSuporte().getUsuario().getId())
            throw new BussinesException("Usuário sem permição para alterar esse ticket.");

        var motivo = motivoService.getById(dto.getMotivoId());
        ticket.setMotivo(motivo);

        var categoria = dto.getCategoriaId() != null
                ? categoriaService.getById(dto.getCategoriaId())
                : ticket.getCategoria();
        ticket.setCategoria(categoria);

        var prioridade = dto.getPrioridadeId() != null
                ? prioridadeService.getById(dto.getPrioridadeId())
                : ticket.getPrioridade();
        ticket.setPrioridade(prioridade);

        ticket.setSubStatus(dto.getSubStatusId());

        calculadoraPrazoTicketService.calcularPrazosPrimeiraRespostaEResolucao(ticket);

        respository.save(ticket);
    }
}

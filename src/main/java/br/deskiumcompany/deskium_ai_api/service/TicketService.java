package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Anexo;
import br.deskiumcompany.deskium_ai_api.domain.Ticket;
import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.domain.enums.Status;
import br.deskiumcompany.deskium_ai_api.domain.enums.SubStatus;
import br.deskiumcompany.deskium_ai_api.domain.enums.TipoUsuario;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.respository.TicketRespository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private SuporteService suporteService;

    @Autowired
    private TicketRespository respository;

    @Autowired
    private AcaoService acaoService;

    @Autowired
    private ArquivosService arquivosService;

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

        return respository.save(ticket);
    }

    public List<Ticket> getAllTickts(Usuario usuario, Status status, Long ticketId,
                                     String assunto, String suporte, String solicitante, SubStatus subStatus,
                                     Long motivoId, Long categoriaId, boolean allTickets) {

        //Se for solicitante, retorna apenas os tickets dele.
        if(usuario.getTipoUsuario().equals(TipoUsuario.SOLICITANTE)){
            return respository.findAll(usuario, status, ticketId, assunto, suporte,
                    subStatus, motivoId, categoriaId, "");

        //Se não for, ele é suporte. Caso a flag allTickets seja true, retorna todos os tickets de todos os usuários.
        }else if(allTickets){
            return respository.findAll(null, status, ticketId, assunto, suporte,
                    subStatus, motivoId, categoriaId, solicitante);

        //se for falso, retorno os tickets do suporte
        }else {
            return respository.findAll(usuario, status, ticketId, assunto, "",
                    subStatus, motivoId, categoriaId, solicitante);
        }
    }

    public Ticket getById(Long id, Usuario usuario){
        //Valida se o solicitante pode requisitar o ticket do ID informado.
        if(usuario.getTipoUsuario() == TipoUsuario.SOLICITANTE){
            return respository.findByIdAndSolicitanteUsuarioId(id, usuario.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Ticket não encontrado."));
        }else {
            return respository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Ticket não encontrado."));
        }
    }
}

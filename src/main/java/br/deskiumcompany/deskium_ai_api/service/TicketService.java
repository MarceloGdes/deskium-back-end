package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Anexo;
import br.deskiumcompany.deskium_ai_api.domain.Ticket;
import br.deskiumcompany.deskium_ai_api.domain.Usuario;
import br.deskiumcompany.deskium_ai_api.domain.enums.Status;
import br.deskiumcompany.deskium_ai_api.domain.enums.SubStatus;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
import br.deskiumcompany.deskium_ai_api.respository.TicketRespository;
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
        acao.setTicket(ticket);

        if(acao.getAnexos() != null && !acao.getAnexos().isEmpty()){
            for (Anexo anexo : acao.getAnexos()) {
                if (acaoService.arquivoExistsByFileName(anexo.getFileName())) {
                    anexo.setAcao(acao);
                } else {
                    throw new BussinesException("o arquivo: " + anexo.getFileName() + " não existe. Faça upload novamente.");
                }
            }
        }

        return respository.save(ticket);
    }

    public List<Ticket> getAllTickts(Usuario usuario, Status status, Long ticketId,
                                     String assunto, String suporte, SubStatus subStatus,
                                     Long motivoId, Long categoriaId) {

        return respository.findAllBySolicitante(usuario, status, ticketId, assunto, suporte,
                        subStatus, motivoId, categoriaId);

    }
}

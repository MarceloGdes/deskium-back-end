package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Ticket;
import br.deskiumcompany.deskium_ai_api.respository.TicketRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Ticket insert(Ticket ticket){
        var solicitante = ticket.getSolicitante();
        solicitante = solicitanteService.getByUsuarioId(solicitante.getUsuario().getId());

        var categoria = ticket.getCategoria();
        categoria = categoriaService.getById(categoria.getId());

        var motivo = ticket.getMotivo();
        motivo = motivoService.getById(motivo.getId());

        ticket.setMotivo(motivo);
        ticket.setCategoria(categoria);
        ticket.setSolicitante(solicitante);
        ticket.setSuporte(suporteService.getSuporteComMenosTickets());

        //Test TODO: Remover posteriormente
        //Referencia  o objeto acao em memoria
        var acao = ticket.getAcoes().getFirst();
        acao.setTextoPuro(acao.getHtml());
        acao.setTicket(ticket);

        return respository.save(ticket);
    }
}

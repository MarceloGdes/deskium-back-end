package br.deskiumcompany.deskium_ai_api.service;

import br.deskiumcompany.deskium_ai_api.domain.Anexo;
import br.deskiumcompany.deskium_ai_api.domain.Ticket;
import br.deskiumcompany.deskium_ai_api.exception.BussinesException;
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

    @Autowired
    private AcaoService acaoService;

    public Ticket insert(Ticket ticket) throws BussinesException{
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
}

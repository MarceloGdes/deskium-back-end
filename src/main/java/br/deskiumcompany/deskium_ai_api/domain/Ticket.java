package br.deskiumcompany.deskium_ai_api.domain;

import br.deskiumcompany.deskium_ai_api.domain.enums.OrigemAcao;
import br.deskiumcompany.deskium_ai_api.domain.enums.Status;
import br.deskiumcompany.deskium_ai_api.domain.enums.SubStatus;
import br.deskiumcompany.deskium_ai_api.dto.arquivo.ArquivoDTO;
import br.deskiumcompany.deskium_ai_api.dto.ticket.TicketInsertDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket extends EntidadeBase{
    @Column(nullable = false, length = 100)
    private String titulo;

    private LocalDateTime previsaoResolucao;
    private LocalDateTime dataResolucao;
    private LocalDateTime previsaoPrimeiraResposta;
    private LocalDateTime dataPrimeiraResposta;
    private LocalTime horasApontadas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @ManyToOne(optional = false)
    private Solicitante solicitante;

    @ManyToOne(optional = false)
    private Suporte suporte;

    @ManyToOne()
    private Motivo motivo;

    @ManyToOne()
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubStatus subStatus;

    @ManyToOne()
    private Prioridade prioridade;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<Acao> acoes;

    public Ticket(TicketInsertDTO dto, Usuario usuario) {
        this.titulo = dto.getTitulo();
        this.status = Status.ABERTO;

        var solicitante = new Solicitante();
        solicitante.setUsuario(usuario);
        this.solicitante = solicitante;

        var motivo = new Motivo();
        motivo.setId(dto.getMotivoId());
        this.motivo = motivo;

        if(dto.getCategoriaId() != null){
            var categoria = new Categoria();
            categoria.setId(dto.getCategoriaId());
            this.categoria = categoria;
        }

        this.subStatus = SubStatus.NOVO;

        Acao acao = new Acao();
        acao.setAcaoInterna(false);
        acao.setNumAcao(1);
        acao.setOrigemAcao(OrigemAcao.SISTEMA);
        acao.setHtml(dto.getDescricaoHtml());
        acao.setUsuarioAutor(usuario);

        if(dto.getArquivos() != null && !dto.getArquivos().isEmpty()){
            List<Anexo> anexos = new ArrayList<>();

            for (ArquivoDTO arquivoDTO : dto.getArquivos()){
                Anexo anexo = new Anexo();
                anexo.setFileName(arquivoDTO.getFileName());
                anexos.add(anexo);
            }

            acao.setAnexos(anexos);
        }

        this.acoes = new ArrayList<>();
        this.acoes.add(acao);
    }
}

package br.deskiumcompany.deskium_ai_api.domain;

import br.deskiumcompany.deskium_ai_api.domain.enums.OrigemAcao;
import br.deskiumcompany.deskium_ai_api.domain.enums.TipoUsuario;
import br.deskiumcompany.deskium_ai_api.dto.acao.AcaoInsertDTO;
import br.deskiumcompany.deskium_ai_api.dto.arquivo.ArquivoDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Acao extends EntidadeBase {

    @ManyToOne(optional = false, cascade = CascadeType.PERSIST) //Utilizado o cascade para gravar atualizações de SubStatus
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @Column(nullable = false)
    private int numAcao;

    @Column(nullable = false)
    private boolean acaoInterna;

    @Column(nullable = false)
    private boolean acaoTranscricao;

    private LocalDate dataAtendimento;
    private LocalTime inicioAtendimento;
    private LocalTime fimAtendimento;

    @ManyToOne(optional = false)
    private Usuario usuarioAutor;

    //para não perder a formatação, irei armazenar o própio HTML
    //Text -> sem limite de caracteres
    @Column(columnDefinition = "TEXT", nullable = false)
    private String html;

    // Texto puro, para buscas aprimoradas
    // Limite de 10k de caracteres.
    //TODO: Implementar validação no front e antes de salvar no banco
    @Column(length = 10000, nullable = false)
    private String textoPuro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrigemAcao origemAcao;

    @OneToMany(mappedBy = "acao", cascade = CascadeType.ALL)
    private List<Anexo> anexos;

    public Acao(Ticket ticket, Usuario usuario, AcaoInsertDTO dto, OrigemAcao origemAcao) {
        this.ticket = ticket;
        this.usuarioAutor = usuario;
        this.acaoInterna = usuario.getTipoUsuario() == TipoUsuario.SOLICITANTE
                ? false
                : dto.isAcaoInterna();
        this.html = dto.getHtml();
        this.origemAcao = origemAcao;
        this.acaoTranscricao = usuario.getTipoUsuario() == TipoUsuario.SOLICITANTE
                ? false
                : dto.isAcaoTranscricao();

        this.dataAtendimento = dto.getDataAtendimento();
        this.inicioAtendimento = dto.getInicioAtendimento();
        this.fimAtendimento = dto.getFimAtendimento();

        if(dto.getAnexos() != null && !dto.getAnexos().isEmpty()){
            this.anexos = new ArrayList<>();

            for (ArquivoDTO arquivoDTO : dto.getAnexos()){
                Anexo anexo = new Anexo();
                anexo.setFileName(arquivoDTO.getFileName());
                this.anexos.add(anexo);
            }
        }
    }
}

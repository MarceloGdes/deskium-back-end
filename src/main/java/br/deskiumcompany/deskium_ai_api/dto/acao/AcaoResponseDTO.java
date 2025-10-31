package br.deskiumcompany.deskium_ai_api.dto.acao;

import br.deskiumcompany.deskium_ai_api.domain.Acao;
import br.deskiumcompany.deskium_ai_api.domain.enums.OrigemAcao;
import br.deskiumcompany.deskium_ai_api.dto.arquivo.ArquivoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcaoResponseDTO {
    private int numAcao;
    private LocalDateTime criadoEm;
    private boolean acaoInterna;
    private LocalDate dataAtendimento;
    private LocalTime inicioAtendimento;
    private LocalTime fimAtendimento;
    private String autor;
    private String html;
    private OrigemAcao origemAcao;
    private List<ArquivoDTO> anexos;


    public AcaoResponseDTO(Acao acao) {
        this.numAcao = acao.getNumAcao();
        this.criadoEm = acao.getCriadoEm();
        this.acaoInterna = acao.isAcaoInterna();
        this.dataAtendimento = acao.getDataAtendimento();
        this.inicioAtendimento = acao.getInicioAtendimento();
        this.fimAtendimento = acao.getFimAtendimento();
        this.autor = acao.getUsuarioAutor().getNomeCompleto();
        this.html = acao.getHtml();
        this.origemAcao = acao.getOrigemAcao();

        if(acao.getAnexos() != null &&!acao.getAnexos().isEmpty()){
            this.anexos = new ArrayList<>();
            acao.getAnexos().forEach(a -> {
                this.anexos.add(new ArquivoDTO(a.getFileName()));
            });
        }

    }
}

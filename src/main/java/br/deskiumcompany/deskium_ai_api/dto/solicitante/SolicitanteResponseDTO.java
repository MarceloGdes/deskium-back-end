package br.deskiumcompany.deskium_ai_api.dto.solicitante;

import br.deskiumcompany.deskium_ai_api.domain.Empresa;
import br.deskiumcompany.deskium_ai_api.domain.Solicitante;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SolicitanteResponseDTO {
    private Long id;
    private String nomeCompleto;
    private String email;
    private String celular;
    private String telefone;
    private Empresa empresa;
    private String cargo;
    private String setor;
    private String observacoes;
    private boolean ativo;


    public SolicitanteResponseDTO(Solicitante solicitante) {
        this.id = solicitante.getId();
        this.nomeCompleto = solicitante.getUsuario().getNomeCompleto();
        this.email = solicitante.getUsuario().getEmail();
        this.celular = solicitante.getCelular();
        this.telefone = solicitante.getTelefone();
        this.empresa = solicitante.getEmpresa();
        this.cargo = solicitante.getCargo();
        this.setor = solicitante.getSetor();
        this.observacoes = solicitante.getObservacoes();
        this.ativo = solicitante.getUsuario().isAtivo();
    }
}

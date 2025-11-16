package br.deskiumcompany.deskium_ai_api.domain;

import br.deskiumcompany.deskium_ai_api.dto.solicitante.SolicitanteInsertDTO;
import br.deskiumcompany.deskium_ai_api.dto.solicitante.SolicitanteUpdateDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Solicitante extends EntidadeBase{
    @Column(nullable = false, length = 50)
    private String cargo;

    @Column(nullable = false, length = 50)
    private String setor;

    @Column(length = 11)
    private String celular;

    @Column(nullable = false, length = 10)
    private String telefone;

    private String observacoes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Solicitante(SolicitanteInsertDTO dto) {
        this.cargo = dto.getCargo();
        this.setor = dto.getSetor();
        this.celular = dto.getCelular();
        this.telefone = dto.getTelefone();
        this.observacoes = dto.getObservacoes();

        Empresa empresa = new Empresa();
        empresa.setId(dto.getEmpresaId());
        this.empresa = empresa;

        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        this.usuario = usuario;
    }

    public Solicitante(SolicitanteUpdateDto dto) {
        this.cargo = dto.getCargo();
        this.setor = dto.getSetor();
        this.celular = dto.getCelular();
        this.telefone = dto.getTelefone();
        this.observacoes = dto.getObservacoes();

        Usuario usuario = new Usuario();
        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha());
        usuario.setAtivo(dto.isAtivo());
        this.usuario = usuario;
    }
}

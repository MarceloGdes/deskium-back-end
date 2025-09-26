package br.deskiumcompany.deskium_ai_api.domain;

import br.deskiumcompany.deskium_ai_api.domain.enums.PerfilSuporte;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Suporte extends EntidadeBase{
    @Enumerated(EnumType.STRING) //Armazena o texto do ENUM no banco.
    private PerfilSuporte pefil;

    @OneToMany(mappedBy = "suporte")
    private List<Ticket> tickets;

    @OneToOne(optional = false)
    private Usuario usuario;
}

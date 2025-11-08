package br.deskiumcompany.deskium_ai_api.domain;

import br.deskiumcompany.deskium_ai_api.domain.enums.DiaSemana;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expediente {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private DiaSemana diaSemana;

    @Column(nullable = false)
    private boolean expediente;

    private LocalTime inicioManha;

    private LocalTime fimManha;

    private LocalTime inicioTarde;

    private LocalTime fimTarde;
}

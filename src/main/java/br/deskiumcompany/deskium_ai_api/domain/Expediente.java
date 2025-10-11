package br.deskiumcompany.deskium_ai_api.domain;

import br.deskiumcompany.deskium_ai_api.domain.enums.DiaSemana;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expediente extends EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private DiaSemana diaSemana;

    @Column(nullable = false)
    private boolean expediente;

    @Column(nullable = false)
    private LocalTime inicioManha;

    @Column(nullable = false)
    private LocalTime fimManha;

    @Column(nullable = false)
    private LocalTime inicioTarde;

    @Column(nullable = false)
    private LocalTime fimTarde;
}

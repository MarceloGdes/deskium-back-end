package br.deskiumcompany.deskium_ai_api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Motivo extends EntidadeBase {
    @Column(nullable = false, length = 100)
    private String descricao;

    @Column(nullable = false)
    private int prazoPrimeiraResposta;

    @Column(nullable = false)
    private int prazoResolucao;
}

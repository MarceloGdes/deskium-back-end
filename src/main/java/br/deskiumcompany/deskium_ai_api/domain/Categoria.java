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
public class Categoria extends EntidadeBase {
    @Column(nullable = false, length = 50)
    private String descricao;
    @Column(nullable = false)
    private int decressimoPrazoPrimeiraResposta;
    @Column(nullable = false)
    private int decressimoPrazoResolucao;
}

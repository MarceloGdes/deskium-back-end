package br.deskiumcompany.deskium_ai_api.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfiguracoesGerais{
    @Id
    @Column(length = 50)
    private String descConfig;
    @Column(nullable = false, length = 50)
    private String valor;
}

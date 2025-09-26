package br.deskiumcompany.deskium_ai_api.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@MappedSuperclass //Todas as entidades que estendem a classe herdarão corretamente os atributos, com as anotações
public abstract class EntidadeBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime criadoEm;
}


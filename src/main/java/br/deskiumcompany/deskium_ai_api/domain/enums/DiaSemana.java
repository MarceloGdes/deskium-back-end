package br.deskiumcompany.deskium_ai_api.domain.enums;

public enum DiaSemana {
    MONDAY("Segunda-Feira"),
    TUESDAY("Terça-Feira"),
    WEDNESDAY("Quarta-Feira"),
    THURSDAY("Quinta-Feira"),
    FRIDAY("Sexta-Feira"),
    SATURDAY("Sabádo"),
    SUNDAY("Domingo");

    private String descricao;

    DiaSemana(String descricao){
        this.descricao = descricao;
    }
}

package br.deskiumcompany.deskium_ai_api.domain.enums;

public enum Status {
    ABERTO("Aberto"),
    RESOLVIDO("Resolvido"),
    CANCELADO("Cancelado");

    private final String desc;

    Status (String desc){
        this.desc = desc;
    }

    public String getDescricao() {
        return desc;
    }
}

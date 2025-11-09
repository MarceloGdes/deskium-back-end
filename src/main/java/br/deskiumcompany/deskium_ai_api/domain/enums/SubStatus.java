package br.deskiumcompany.deskium_ai_api.domain.enums;

public enum SubStatus {
    NOVO("Novo"),
    EM_ATENDIMENTO("Em atendimento"),
    AGUARDANDO_RETORNO("Aguardando retono"),

//    Apenas para controle interno.
    FECHADO("Fechado");

    private final String desc;

    SubStatus (String desc){
        this.desc = desc;
    }

    public String getDescricao() {
        return desc;
    }
}

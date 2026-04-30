package com.projeto.manager.models.enums;

public enum StatusProjeto {

    EM_ANALISE("Em análise"),
    ANALISE_REALIZADA("Análise realizada"),
    ANALISE_APROVADA("Análise aprovada"),
    INICIADO("Iniciado"),
    PLANEJADO("Planejado"),
    EM_ANDAMENTO("Em andamento"),
    ENCERRADO("ENCERRADO"),
    CANCELADO("CANCELADO")
    ;

    StatusProjeto(String s) {
    }
}

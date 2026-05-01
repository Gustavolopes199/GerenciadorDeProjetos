package com.projeto.manager.rules;

import com.projeto.manager.models.enums.StatusProjeto;
import org.springframework.stereotype.Component;



@Component
public class StatusProjetoService {

    public boolean podeAtualizarStatusProjeto(StatusProjeto statusProjetoAtual, StatusProjeto statusProjetoDestino){
        if (statusProjetoDestino == StatusProjeto.CANCELADO) return true;
        if (statusProjetoAtual == StatusProjeto.CANCELADO) return false;
        if (statusProjetoAtual == StatusProjeto.ENCERRADO) return false;
        if (statusProjetoAtual == statusProjetoDestino) return true;
        if (statusProjetoAtual == StatusProjeto.EM_ANALISE && statusProjetoDestino == StatusProjeto.ANALISE_REALIZADA) return true;
        if (statusProjetoAtual == StatusProjeto.ANALISE_REALIZADA && statusProjetoDestino == StatusProjeto.ANALISE_APROVADA) return true;
        if (statusProjetoAtual == StatusProjeto.ANALISE_APROVADA && statusProjetoDestino == StatusProjeto.INICIADO) return true;
        if (statusProjetoAtual == StatusProjeto.INICIADO && statusProjetoDestino == StatusProjeto.PLANEJADO) return true;
        if (statusProjetoAtual == StatusProjeto.PLANEJADO && statusProjetoDestino == StatusProjeto.EM_ANDAMENTO) return true;
        return statusProjetoAtual == StatusProjeto.EM_ANDAMENTO && statusProjetoDestino == StatusProjeto.ENCERRADO;
    }


    public boolean podeExcluirProjeto(StatusProjeto statusProjeto){
        if (statusProjeto == StatusProjeto.INICIADO) return false;
        if (statusProjeto == StatusProjeto.EM_ANDAMENTO) return false;
        return statusProjeto != StatusProjeto.ENCERRADO;
    }

}

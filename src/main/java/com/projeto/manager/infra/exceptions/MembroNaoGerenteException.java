package com.projeto.manager.infra.exceptions;

public class MembroNaoGerenteException extends RuntimeException{
    public MembroNaoGerenteException(String message){
        super("Membro não é gerente: " + message);
    }
}

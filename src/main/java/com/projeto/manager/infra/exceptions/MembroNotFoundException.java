package com.projeto.manager.infra.exceptions;

public class MembroNotFoundException extends RuntimeException{
    public MembroNotFoundException(String message){
        super("Membro não encontrado: " + message);
    }
}

package com.projeto.manager.infra.exceptions;

public class StatusNaoPermitidoException extends RuntimeException{
    public StatusNaoPermitidoException(String message){
        super(message);
    }
}

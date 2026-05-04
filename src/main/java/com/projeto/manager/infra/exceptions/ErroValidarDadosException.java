package com.projeto.manager.infra.exceptions;

public class ErroValidarDadosException extends RuntimeException{
    public ErroValidarDadosException(String message){
        super(message);
    }
}

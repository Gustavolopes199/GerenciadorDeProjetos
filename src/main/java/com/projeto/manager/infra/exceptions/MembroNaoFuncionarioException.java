package com.projeto.manager.infra.exceptions;

public class MembroNaoFuncionarioException extends RuntimeException {
    public MembroNaoFuncionarioException(String message) {
        super(message);
    }
}

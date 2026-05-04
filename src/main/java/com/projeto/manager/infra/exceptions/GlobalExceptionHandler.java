package com.projeto.manager.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErroApi> build(HttpStatus status, String code, String message, HttpServletRequest request){
        return ResponseEntity.status(status).body(
                new ErroApi(code,
                        message,
                        status.value(),
                        request.getRequestURI(),
                        OffsetDateTime.now())
        );
    }

    @ExceptionHandler(MembroNotFoundException.class)
    public ResponseEntity<ErroApi> handleMembroNotFound(MembroNotFoundException ex, HttpServletRequest request){
        return build(HttpStatus.NOT_FOUND, "MEMBRO_NOT_FOUND", ex.getMessage(), request);
    }

    @ExceptionHandler(StatusNaoPermitidoException.class)
    public ResponseEntity<ErroApi> handleStatusNaoPermitidoException(StatusNaoPermitidoException ex, HttpServletRequest request){
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "STATUS_NAO_PERMITIDO", ex.getMessage(), request);
    }

    @ExceptionHandler(MembroNaoGerenteException.class)
    public ResponseEntity<ErroApi> handleMembroNaoGerenteException(MembroNaoGerenteException ex, HttpServletRequest request){
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "MEMBRO_NAO_GERENTE", ex.getMessage(), request);
    }

    @ExceptionHandler(MembroNaoFuncionarioException.class)
    public ResponseEntity<ErroApi> handleMembroNaoFuncionarioException(MembroNaoFuncionarioException ex, HttpServletRequest request){
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "MEMBRO_NAO_GERENTE", ex.getMessage(), request);
    }

    @ExceptionHandler(CargoNotFoundException.class)
    public ResponseEntity<ErroApi> handleCargoNotFounException(CargoNotFoundException ex, HttpServletRequest request){
        return build(HttpStatus.BAD_REQUEST, "CARGO_NAO_ENCONTRADO", ex.getMessage(), request);
    }

    @ExceptionHandler(QuantidadePorFuncionarioException.class)
    public ResponseEntity<ErroApi> handleQuantiadeExcedida(QuantidadePorFuncionarioException ex, HttpServletRequest request){
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "CARGAO_NAO_ENCONTRADO", ex.getMessage(), request);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroApi> handleEntidadeNaoEncontrada(EntityNotFoundException ex, HttpServletRequest request){
        return build(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), request);
    }

    @ExceptionHandler(ErroValidarDadosException.class)
    public ResponseEntity<ErroApi> handleErroAoValidarDados(ErroValidarDadosException ex, HttpServletRequest request){
        return build(HttpStatus.UNPROCESSABLE_ENTITY, "ERRO_VALIDACAO", ex.getMessage(), request);
    }


}

package com.projeto.manager.controllers;

import com.projeto.manager.models.dtos.projeto.CadastroProjeto;
import com.projeto.manager.models.enums.RiscoProjeto;
import com.projeto.manager.models.enums.StatusProjeto;
import com.projeto.manager.services.ProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/projeto")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService service;

    @PostMapping
    public ResponseEntity<?> cadastrarProjeto(@RequestBody CadastroProjeto data){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarProjeto(data));
    }

}

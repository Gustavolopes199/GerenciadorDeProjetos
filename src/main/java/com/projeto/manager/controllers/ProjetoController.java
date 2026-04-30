package com.projeto.manager.controllers;

import com.projeto.manager.models.enums.RiscoProjeto;
import com.projeto.manager.models.enums.StatusProjeto;
import com.projeto.manager.services.ProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService service;






}

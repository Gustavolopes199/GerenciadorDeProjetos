package com.projeto.manager.controllers;

import com.projeto.manager.models.dtos.projeto.CadastroProjeto;
import com.projeto.manager.models.enums.RiscoProjeto;
import com.projeto.manager.models.enums.StatusProjeto;
import com.projeto.manager.services.AlocarMembroCase;
import com.projeto.manager.services.ProjetoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/projeto")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService service;
    private final AlocarMembroCase alocarMembroCase;

    @PostMapping
    public ResponseEntity<?> cadastrarProjeto(@RequestBody CadastroProjeto data){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarProjeto(data));
    }

    @GetMapping
    public ResponseEntity<?> buscarProjetos(
            @RequestParam(required = false, defaultValue = "50") int size,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFinal,
            @RequestParam(required = false) BigDecimal valorOrcamentoInicial,
            @RequestParam(required = false) BigDecimal valorOrcamentoFinal,
            @RequestParam(required = false) List<StatusProjeto> statusProjeto,
            @RequestParam(required = false) List<Long> idGerenteList,
            @RequestParam(required = false) List<Long> idFuncionarioList
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                service.buscaFiltrada(
                        size,
                        page,
                        nome,
                        dataInicio,
                        dataFinal,
                        valorOrcamentoInicial,
                        valorOrcamentoFinal,
                        statusProjeto,
                        idGerenteList,
                        idFuncionarioList)
        );
    }

}

package com.projeto.manager.models.dtos.projeto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CadastroProjeto {

    private String nome;

    private LocalDate dataInicio;

    private LocalDate previsaoTermino;

    private LocalDate dataTermino;

    private BigDecimal orcamentoTotal;

    private String descricao;

    private Long idGerente;

    private List<Long> funcionarios;

}

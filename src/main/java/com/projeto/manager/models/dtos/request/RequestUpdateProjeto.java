package com.projeto.manager.models.dtos.request;

import com.projeto.manager.models.enums.StatusProjeto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RequestUpdateProjeto {

    private String nome;

    private LocalDate dataInicio;

    private LocalDate previsaoTermino;

    private LocalDate dataTermino;

    private BigDecimal orcamentoTotal;

    private String descricao;

    private StatusProjeto statusProjeto;

}

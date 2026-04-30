package com.projeto.manager.models.dtos.projeto;

import com.projeto.manager.models.enums.RiscoProjeto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseProjetoCadastrado {

    private long id;

    private String nome;

    private String descricao;

    private RiscoProjeto riscoProjeto;

    private BigDecimal orcamentoTotal;

    private String gerente;

}

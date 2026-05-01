package com.projeto.manager.rules;

import com.projeto.manager.models.enums.RiscoProjeto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class CalculoProjetoRisco {

    private static final int limite_meses_baixo = 3;
    private static final int limite_meses_alto = 6;
    private static final BigDecimal limite_orcamento_baixo = BigDecimal.valueOf(100000);
    private static final BigDecimal limite_orcamento_alto = BigDecimal.valueOf(500000);


    public RiscoProjeto calcular(BigDecimal valorOrcamento,
                                 LocalDate dataInicio,
                                 LocalDate dataPrevisaoTermino){
        long meses = ChronoUnit.MONTHS.between(dataInicio, dataPrevisaoTermino);


        if (meses > limite_meses_alto || valorOrcamento.compareTo(limite_orcamento_alto) > 0) return RiscoProjeto.ALTO;


        if (meses <= limite_meses_baixo && valorOrcamento.compareTo(limite_orcamento_baixo) <= 0) return RiscoProjeto.BAIXO;

        return RiscoProjeto.MEDIO;

    }

}

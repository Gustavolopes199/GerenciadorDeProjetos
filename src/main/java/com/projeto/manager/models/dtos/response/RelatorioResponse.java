package com.projeto.manager.models.dtos.response;

import com.projeto.manager.models.enums.StatusProjeto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class RelatorioResponse {

    private Map<StatusProjeto, Long> projetosPorStatus;

    private Map<StatusProjeto, BigDecimal> valorPorStatus;

    private Integer mediaProjetosEncerrados;

    private Integer membrosUnicos;

}

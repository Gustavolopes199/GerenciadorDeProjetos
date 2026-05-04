package com.projeto.manager.models.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "Dto para cadastro de produto")
public class CadastroProjeto {

    @NotBlank(message = "Nome em branco")
    private String nome;

    @NotNull(message = "Data de inicio em branco")
    private LocalDate dataInicio;

    @NotNull(message = "Necessário enviar data de termino")
    @Schema(description = "Necessário ser enviado quando quiser marcar o projeto como encerrado",
            example = "2026-01-01")
    private LocalDate previsaoTermino;

    @Schema(description = "Necessário ser enviado quando quiser marcar o projeto como encerrado",
    example = "2026-01-01")
    private LocalDate dataTermino;

    @NotNull
    private BigDecimal orcamentoTotal;

    private String descricao;

    @NotNull
    private Long idGerente;

    private List<Long> funcionarios;

}

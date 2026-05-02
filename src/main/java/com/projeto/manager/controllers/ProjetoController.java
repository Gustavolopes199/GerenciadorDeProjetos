package com.projeto.manager.controllers;

import com.projeto.manager.infra.exceptions.ErroApi;
import com.projeto.manager.models.dtos.projeto.CadastroProjeto;
import com.projeto.manager.models.entity.Projeto;
import com.projeto.manager.models.enums.RiscoProjeto;
import com.projeto.manager.models.enums.StatusProjeto;
import com.projeto.manager.services.AlocarMembroCase;
import com.projeto.manager.services.ProjetoService;
import com.sun.xml.bind.v2.TODO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/projetos")
@RequiredArgsConstructor
@Tag(name = "Projetos", description = "Lida com todas as requisições de projeto")
public class ProjetoController {

    private final ProjetoService service;
    private final AlocarMembroCase alocarMembroCase;

    @PostMapping
    public ResponseEntity<?> cadastrarProjeto(@RequestBody CadastroProjeto data){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarProjeto(data));
    }

    @GetMapping
    @Operation(
            summary = "Busca os projetos com base nos filtros enviados",
            description = "Permite buscar todos os projetos com base no filtro enviado",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Lista paginada de projetos",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Projeto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Não encontrado nenhum registro para os filtros informados",
                            content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = ErroApi.class))
                    )
            }
    )
    public ResponseEntity<?> buscarProjetos(
            @Parameter(name = "Size", description = "Tamanho da página buscada")
            @RequestParam(required = false, defaultValue = "50") int size,
            @Parameter(name = "Page", description = "Pagina para busca")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(name = "Nome", description = "Buscar pelo nome do projeto")
            @RequestParam(required = false) String nome,
            @Parameter(name = "Data Inicio", description = "Data inicial do inicio projeto, funciona somente se informado junto com dataFinal")
            @RequestParam(required = false) LocalDate dataInicio,
            @Parameter(name = "Data Final", description = "Data final do inicio projeto, funciona somente se informado junto com dataInicio")
            @RequestParam(required = false) LocalDate dataFinal,
            @Parameter(name = "Valor orçamento inicial", description = "Valor inicial do projeto, funciona como uma busca por range. Se informado junto com valorOrcamentoFinal ira trazer tudo dentro do intervalo")
            @RequestParam(required = false) BigDecimal valorOrcamentoInicial,
            @Parameter(name = "Valor orçamento final",  description = "Valor final do projeto, funciona como uma busca por range. Se informado junto com valorOrcamentoInicial ira trazer tudo dentro do intervalo")
            @RequestParam(required = false) BigDecimal valorOrcamentoFinal,
            @Parameter(name = "Status Projeto", description = "Status atual do projeto, pode ser passado como Array do tipo String", array = @ArraySchema(schema = @Schema(type = "string")))
            @RequestParam(required = false) List<StatusProjeto> statusProjeto,
            @Parameter(name = "Gerente Id List", description = "Busca os projetos que pertencem aquele gerente",array = @ArraySchema(schema = @Schema(type = "numeric")))
            @RequestParam(required = false) List<Long> idGerenteList,
            @Parameter(name = "Funcionario Id List", description = "Busca os projetos em que aquele funcionario está alocado",array = @ArraySchema(schema = @Schema(type = "numeric")))
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


    @Operation(
            summary = "Busca o relatorio com base nos filtros enviados",
            description = "Permite buscar o relatorio com base no filtro enviado",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Relatorio consultado com successo",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Projeto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Não encontrado nenhum registro para os filtros informados",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErroApi.class))
                    )
            }
    )
    @GetMapping("/relatorio")
    public ResponseEntity<?> buscarRelatorioProjetos(
            @Parameter(name = "Size", description = "Tamanho da página buscada")
            @RequestParam(required = false, defaultValue = "50") int size,
            @Parameter(name = "Page", description = "Pagina para busca")
            @RequestParam(required = false, defaultValue = "0") int page,
            @Parameter(name = "Nome", description = "Buscar pelo nome do projeto")
            @RequestParam(required = false) String nome,
            @Parameter(name = "Data Inicio", description = "Data inicial do inicio projeto, funciona somente se informado junto com dataFinal")
            @RequestParam(required = false) LocalDate dataInicio,
            @Parameter(name = "Data Final", description = "Data final do inicio projeto, funciona somente se informado junto com dataInicio")
            @RequestParam(required = false) LocalDate dataFinal,
            @Parameter(name = "Valor orçamento inicial", description = "Valor inicial do projeto, funciona como uma busca por range. Se informado junto com valorOrcamentoFinal ira trazer tudo dentro do intervalo")
            @RequestParam(required = false) BigDecimal valorOrcamentoInicial,
            @Parameter(name = "Valor orçamento final",  description = "Valor final do projeto, funciona como uma busca por range. Se informado junto com valorOrcamentoInicial ira trazer tudo dentro do intervalo")
            @RequestParam(required = false) BigDecimal valorOrcamentoFinal,
            @Parameter(name = "Status Projeto", description = "Status atual do projeto, pode ser passado como Array do tipo String", array = @ArraySchema(schema = @Schema(type = "string")))
            @RequestParam(required = false) List<StatusProjeto> statusProjeto,
            @Parameter(name = "Gerente Id List", description = "Busca os projetos que pertencem aquele gerente",array = @ArraySchema(schema = @Schema(type = "numeric")))
            @RequestParam(required = false) List<Long> idGerenteList,
            @Parameter(name = "Funcionario Id List", description = "Busca os projetos em que aquele funcionario está alocado",array = @ArraySchema(schema = @Schema(type = "numeric")))
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

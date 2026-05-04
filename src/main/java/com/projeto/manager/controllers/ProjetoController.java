package com.projeto.manager.controllers;

import com.projeto.manager.infra.exceptions.ErroApi;
import com.projeto.manager.models.dtos.request.CadastroProjeto;
import com.projeto.manager.models.dtos.request.RequestUpdateProjeto;
import com.projeto.manager.models.dtos.response.ResponseProjetoCadastrado;
import com.projeto.manager.models.dtos.request.ReqAlocarMembroProjeto;
import com.projeto.manager.models.entity.Projeto;
import com.projeto.manager.models.enums.StatusProjeto;
import com.projeto.manager.services.AlocarMembroCase;
import com.projeto.manager.services.ProjetoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/projetos")
@RequiredArgsConstructor
@Tag(name = "Projetos", description = "Lida com todas as requisições de projeto")
public class ProjetoController {

    private final ProjetoService service;
    private final AlocarMembroCase alocarMembroCase;

    @PostMapping
    @Operation(
            summary = "Realiza o cadastro de projetos",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Cadastro realizado com sucesso",
                            content = @Content(mediaType = "application/json",
                                                schema = @Schema(implementation = ResponseProjetoCadastrado.class))
                    ),
                    @ApiResponse(
                            responseCode = "422",
                            description = "Erro no cadastro",
                            content = @Content(mediaType = "application/json",
                                                schema = @Schema(implementation = ErroApi.class)
                            )
                    )
            }
    )
    public ResponseEntity<?> cadastrarProjeto(@Valid @RequestBody CadastroProjeto data){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarProjeto(data));
    }

    @PostMapping("/membro")
    public ResponseEntity<?> alocarMembro(@Valid @RequestBody ReqAlocarMembroProjeto data){
        return ResponseEntity.status(HttpStatus.OK).body(alocarMembroCase.execute(data));
    }

    @GetMapping
    @Operation(
            summary = "Busca os projetos com base nos filtros enviados",
            description = "Permite buscar todos os projetos com base no filtro enviado",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Lista paginada de projetos",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CadastroProjeto.class))
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
            @Parameter( description = "Buscar pelo nome do projeto")
            @RequestParam(required = false) String nome,
            @Parameter(example = "YYYY-MM-DD", description = "Data inicial do inicio projeto, funciona somente se informado junto com dataFinal")
            @RequestParam(required = false) LocalDate dataInicio,
            @Parameter( example = "YYYY-MM-DD", description = "Data final do inicio projeto, funciona somente se informado junto com dataInicio")
            @RequestParam(required = false) LocalDate dataFinal,
            @Parameter( description = "Valor inicial do projeto, funciona como uma busca por range. Se informado junto com valorOrcamentoFinal ira trazer tudo dentro do intervalo")
            @RequestParam(required = false) BigDecimal valorOrcamentoInicial,
            @Parameter(  description = "Valor final do projeto, funciona como uma busca por range. Se informado junto com valorOrcamentoInicial ira trazer tudo dentro do intervalo")
            @RequestParam(required = false) BigDecimal valorOrcamentoFinal,
            @Parameter(description = "Status atual do projeto, pode ser passado como Array do tipo String")
            @RequestParam(required = false) List<StatusProjeto> statusProjeto,
            @Parameter( description = "Busca os projetos que pertencem aquele gerente",array = @ArraySchema(schema = @Schema(type = "numeric")))
            @RequestParam(required = false) List<Long> idGerenteList,
            @Parameter( description = "Busca os projetos em que aquele funcionario está alocado",array = @ArraySchema(schema = @Schema(type = "numeric")))
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
                service.buscaRelatorio(
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarProjeto(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(service.deletarProjeto(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProjeto(@PathVariable Long id, @RequestBody RequestUpdateProjeto data){
        return ResponseEntity.status(HttpStatus.OK).body(service.atualizarProjeto(id, data));
    }



}

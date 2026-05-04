package com.projeto.manager.services;

import com.projeto.manager.infra.exceptions.StatusNaoPermitidoException;
import com.projeto.manager.infra.mappers.ProjetoMapper;
import com.projeto.manager.infra.repositorys.ProjetoRepository;
import com.projeto.manager.infra.specifications.ProjetoSpecification;
import com.projeto.manager.models.dtos.request.CadastroProjeto;
import com.projeto.manager.models.dtos.request.RequestUpdateProjeto;
import com.projeto.manager.models.dtos.response.ResponseProjetoCadastrado;
import com.projeto.manager.models.dtos.response.RelatorioResponse;
import com.projeto.manager.models.entity.Projeto;
import com.projeto.manager.models.enums.StatusProjeto;
import com.projeto.manager.rules.CalculoProjetoRisco;
import com.projeto.manager.rules.StatusProjetoService;
import com.projeto.manager.rules.ValidarCriarAtualizarProjeto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final MembroService membroService;
    private final ProjetoMapper mapper;
    private final CalculoProjetoRisco calculoProjetoRisco;
    private final CalcularRelatorioCase calcularRelatorioCase;
    private final ValidarCriarAtualizarProjeto validarCriarAtualizarProjeto;
    private final StatusProjetoService statusProjetoService;

    public ResponseProjetoCadastrado criarProjeto(CadastroProjeto data){
        Projeto entity = mapper.toEntity(data);

        entity.setGerente(membroService.buscarGerente(data.getIdGerente()));

        entity.setStatusProjeto(StatusProjeto.EM_ANALISE);

        validarCriarAtualizarProjeto.validar(entity);

        entity = projetoRepository.save(entity);

        ResponseProjetoCadastrado response = mapper.toDto(entity);

        response.setRiscoProjeto(calculoProjetoRisco.calcular(
                entity.getOrcamentoTotal(),
                entity.getDataInicio(),
                entity.getPrevisaoTermino()
        ));

        return response;

    }

    public RelatorioResponse buscaRelatorio(
            String nome,
            LocalDate dataInicio,
            LocalDate dataFinal,
            BigDecimal valorOrcamentoInicial,
            BigDecimal valorOrcamentoFinal,
            List<StatusProjeto> statusProjeto,
            List<Long> idGerenteList,
            List<Long> idFuncionarioList
    ){

        Specification<Projeto> spec = ProjetoSpecification.filtroBuilder(
                nome,
                dataInicio,
                dataFinal,
                valorOrcamentoInicial,
                valorOrcamentoFinal,
                statusProjeto,
                idGerenteList,
                idFuncionarioList
        );

        List<Projeto> projetoList = projetoRepository.findAll(spec);

        return calcularRelatorioCase.montarRelatorio(projetoList);

    }

    public Page<Projeto> buscaFiltrada(
            int size,
            int page,
            String nome,
            LocalDate dataInicio,
            LocalDate dataFinal,
            BigDecimal valorOrcamentoInicial,
            BigDecimal valorOrcamentoFinal,
            List<StatusProjeto> statusProjeto,
            List<Long> idGerenteList,
            List<Long> idFuncionarioList
    ){
        Pageable pageable = PageRequest.of(page, size);

        Specification<Projeto> spec = ProjetoSpecification.filtroBuilder(
                nome,
                dataInicio,
                dataFinal,
                valorOrcamentoInicial,
                valorOrcamentoFinal,
                statusProjeto,
                idGerenteList,
                idFuncionarioList
        );


        return projetoRepository.findAll(spec, pageable);
    }

    public Projeto atualizarProjeto(Long id, RequestUpdateProjeto data){

        Projeto entity = projetoRepository.findById(id).orElseThrow( () -> new EntityNotFoundException("Projeto não encontrado"));
        if (data.getStatusProjeto() != null){
            if (!statusProjetoService.podeAtualizarStatusProjeto(entity.getStatusProjeto(), data.getStatusProjeto())){
                throw new StatusNaoPermitidoException("Não é possivel atualizar esse status");
            }
        }

        entity = mapper.updateToEntity(entity, data);

        validarCriarAtualizarProjeto.validar(entity);

        projetoRepository.save(entity);

        return entity;

    }


    public String deletarProjeto(Long id){
        Projeto entity = projetoRepository.findById(id).orElseThrow( () -> new EntityNotFoundException("Projeto não encontrado: " + id));

        if (!statusProjetoService.podeExcluirProjeto(entity.getStatusProjeto())){
            throw new StatusNaoPermitidoException("Projeto não pode ser excluido pois esta com o seguinte status: " + entity.getStatusProjeto());
        }

        projetoRepository.delete(entity);

        return "Projeto excluido com sucesso!";
    }

}

package com.projeto.manager.services;

import com.projeto.manager.infra.mappers.ProjetoMapper;
import com.projeto.manager.infra.repositorys.ProjetoRepository;
import com.projeto.manager.infra.specifications.ProjetoSpecification;
import com.projeto.manager.models.dtos.projeto.CadastroProjeto;
import com.projeto.manager.models.dtos.projeto.ResponseProjetoCadastrado;
import com.projeto.manager.models.entity.Projeto;
import com.projeto.manager.models.enums.StatusProjeto;
import com.projeto.manager.rules.CalculoProjetoRisco;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

    public ResponseProjetoCadastrado criarProjeto(CadastroProjeto data){
        Projeto entity = mapper.toEntity(data);

        entity.setGerente(membroService.buscarGerente(data.getIdGerente()));

        entity.setStatusProjeto(StatusProjeto.EM_ANALISE);

        entity = projetoRepository.save(entity);

        ResponseProjetoCadastrado response = mapper.toDto(entity);

        response.setRiscoProjeto(calculoProjetoRisco.calcular(
                entity.getOrcamentoTotal(),
                entity.getDataInicio(),
                entity.getPrevisaoTermino()
        ));

        return response;

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

}

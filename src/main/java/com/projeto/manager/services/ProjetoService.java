package com.projeto.manager.services;

import com.projeto.manager.infra.mappers.ProjetoMapper;
import com.projeto.manager.infra.repositorys.ProjetoRepository;
import com.projeto.manager.models.dtos.projeto.CadastroProjeto;
import com.projeto.manager.models.dtos.projeto.ResponseProjetoCadastrado;
import com.projeto.manager.models.entity.Projeto;
import com.projeto.manager.models.enums.RiscoProjeto;
import com.projeto.manager.models.enums.StatusProjeto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class ProjetoService {

    private final ProjetoRepository repo;
    private final MembroService membroService;
    private final ProjetoMapper mapper;

    public ResponseProjetoCadastrado criarProjeto(CadastroProjeto data){
        Projeto entity = mapper.toEntity(data);

        entity.setGerente(membroService.buscarGerente(data.getIdGerente()));

        entity.setStatusProjeto(StatusProjeto.EM_ANALISE);

        entity = repo.save(entity);

        ResponseProjetoCadastrado response = mapper.toDto(entity);

        response.setRiscoProjeto(calcularRiscoProjeto(
                entity.getOrcamentoTotal(),
                entity.getDataInicio(),
                entity.getPrevisaoTermino()
        ));

        return response;

    }



    private RiscoProjeto calcularRiscoProjeto(BigDecimal valorOrcamento,
                                              LocalDate dataInicio,
                                              LocalDate dataPrevisaoTermino){
        long meses = ChronoUnit.MONTHS.between(dataInicio, dataPrevisaoTermino);

        if (meses > 6 || valorOrcamento.compareTo(BigDecimal.valueOf(500000)) > 0) return RiscoProjeto.ALTO;


        if (meses <= 3 && valorOrcamento.compareTo(BigDecimal.valueOf(100000)) <= 0) return RiscoProjeto.BAIXO;

        return RiscoProjeto.MEDIO;

    }

    private boolean podeAtualizarStatusProjeto(StatusProjeto statusProjetoAtual, StatusProjeto statusProjetoDestino){
        if (statusProjetoDestino == StatusProjeto.CANCELADO) return true;
        if (statusProjetoAtual == StatusProjeto.CANCELADO) return false;
        if (statusProjetoAtual == StatusProjeto.ENCERRADO) return false;
        if (statusProjetoAtual == statusProjetoDestino) return true;
        if (statusProjetoAtual == StatusProjeto.EM_ANALISE && statusProjetoDestino == StatusProjeto.ANALISE_REALIZADA) return true;
        if (statusProjetoAtual == StatusProjeto.ANALISE_REALIZADA && statusProjetoDestino == StatusProjeto.ANALISE_APROVADA) return true;
        if (statusProjetoAtual == StatusProjeto.ANALISE_APROVADA && statusProjetoDestino == StatusProjeto.INICIADO) return true;
        if (statusProjetoAtual == StatusProjeto.INICIADO && statusProjetoDestino == StatusProjeto.PLANEJADO) return true;
        if (statusProjetoAtual == StatusProjeto.PLANEJADO && statusProjetoDestino == StatusProjeto.EM_ANDAMENTO) return true;
        return statusProjetoAtual == StatusProjeto.EM_ANDAMENTO && statusProjetoDestino == StatusProjeto.ENCERRADO;
    }

    private boolean podeExcluirProjeto(StatusProjeto statusProjeto){
        if (statusProjeto == StatusProjeto.INICIADO) return false;
        if (statusProjeto == StatusProjeto.EM_ANDAMENTO) return false;
        return statusProjeto != StatusProjeto.ENCERRADO;
    }

}

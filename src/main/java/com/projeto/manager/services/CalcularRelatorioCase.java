package com.projeto.manager.services;

import com.projeto.manager.models.dtos.response.RelatorioResponse;
import com.projeto.manager.models.entity.Membro;
import com.projeto.manager.models.entity.Projeto;
import com.projeto.manager.models.enums.StatusProjeto;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CalcularRelatorioCase {

    public RelatorioResponse montarRelatorio(List<Projeto> projetoList){

        RelatorioResponse response = new RelatorioResponse();

        response.setProjetosPorStatus(projetosPorStatus(projetoList));

        response.setValorPorStatus(valorPorStatus(projetoList));

        response.setMediaProjetosEncerrados(mediaProjetosEncerrados(projetoList));

        response.setMembrosUnicos(membrosUnicos(projetoList));

        return response;
    }


    private Map<StatusProjeto, Long> projetosPorStatus(List<Projeto> projetoList){
        return projetoList.stream()
                .collect(Collectors.groupingBy(Projeto::getStatusProjeto, Collectors.counting()));

    }

    private Map<StatusProjeto, BigDecimal> valorPorStatus(List<Projeto> projetoList){

        return projetoList.stream()
                .collect(Collectors.groupingBy(Projeto::getStatusProjeto, Collectors.reducing(
                        BigDecimal.ZERO,
                        Projeto::getOrcamentoTotal,
                        BigDecimal::add
                )));

    }

    private Integer mediaProjetosEncerrados(List<Projeto> projetoList){
        List<Projeto> listFiltrada =  projetoList.stream()
                .filter(r -> r.getStatusProjeto() == StatusProjeto.ENCERRADO)
                .filter(r -> r.getDataInicio() != null)
                .filter(r -> r.getDataTermino() != null)
                .collect(Collectors.toList());

        if (listFiltrada.isEmpty()) return 0;

        long dias = 0;
        for (Projeto projeto : listFiltrada) {

            dias +=  ChronoUnit.DAYS.between(projeto.getDataInicio(), projeto.getDataTermino());

        }

        return Math.toIntExact(dias / listFiltrada.size());
    }

    private Integer membrosUnicos(List<Projeto> projetoList){

        Map<Long, String> membros = new HashMap<>();

        for (Projeto projeto : projetoList) {
            membros.put(projeto.getGerente().getId(), projeto.getGerente().getNome());
            for (Membro funcionario : projeto.getFuncionarios()) {
                membros.put(funcionario.getId(), funcionario.getNome());
            }
        }

        return membros.size();

    }

}

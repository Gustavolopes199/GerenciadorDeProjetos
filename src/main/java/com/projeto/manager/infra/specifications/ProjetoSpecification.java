package com.projeto.manager.infra.specifications;

import com.projeto.manager.models.entity.Projeto;
import com.projeto.manager.models.enums.StatusProjeto;
import org.springframework.stereotype.Component;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProjetoSpecification  {

    public static Specification<Projeto> filtroBuilder(String nome,
                                                LocalDate dataInicio,
                                                LocalDate dataFinal,
                                                BigDecimal valorOrcamentoInicial,
                                                BigDecimal valorOrcamentoFinal,
                                                List<StatusProjeto> statusProjeto,
                                                List<Long> idGerenteList,
                                                List<Long> idFuncionarioList){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nome != null ){
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("nome")),
                                nome.toLowerCase()
                        )
                );
            }

            if (dataInicio != null && dataFinal != null){
                predicates.add(
                        criteriaBuilder.between(
                               root.get("dataInicio"),
                                dataInicio, dataFinal
                        )
                );
            }

            if (valorOrcamentoInicial != null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("orcamentoTotal"),
                        valorOrcamentoInicial
                ));
            }
            if (valorOrcamentoFinal != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("orcamentoTotal"),
                        valorOrcamentoFinal
                ));
            }

            if (!statusProjeto.isEmpty()){
                predicates.add(
                        root.get("statusProjeto").in(statusProjeto)
                );
            }

            if (!idGerenteList.isEmpty()){
                predicates.add(
                        root.get("gerente").in(idGerenteList)
                );
            }

            if (!idFuncionarioList.isEmpty()){
                Join<Object, Object> funcionarios = root.join("funcionarios", JoinType.INNER);
                predicates.add(funcionarios.get("id").in(idFuncionarioList));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });

    }


}

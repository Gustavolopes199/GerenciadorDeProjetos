package com.projeto.manager.models.entity;

import com.projeto.manager.models.enums.StatusProjeto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "projetos")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Projeto {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private LocalDate dataInicio;

    private LocalDate previsaoTermino;

    private LocalDate dataTermino;

    private BigDecimal orcamentoTotal;

    @Column(length = 1000)
    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusProjeto statusProjeto;

    @ManyToMany
    @JoinTable(
            name = "projeto_funcionario",
            joinColumns = @JoinColumn(name = "projeto_id"),
            inverseJoinColumns = @JoinColumn(name = "membro_id")
    )
    private Set<Membro> funcionarios;

    @ManyToOne
    @JoinColumn(
            name = "gerente_id",
            referencedColumnName = "id"
    )
    private Membro gerente;

}

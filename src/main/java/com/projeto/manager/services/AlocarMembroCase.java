package com.projeto.manager.services;

import com.projeto.manager.infra.exceptions.ProjetoFuncionarioQuantidadeException;
import com.projeto.manager.infra.exceptions.QuantidadePorFuncionarioException;
import com.projeto.manager.infra.repositorys.ProjetoRepository;
import com.projeto.manager.models.dtos.request.ReqAlocarMembroProjeto;
import com.projeto.manager.models.entity.Membro;
import com.projeto.manager.models.entity.Projeto;
import com.projeto.manager.models.enums.StatusProjeto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlocarMembroCase {

    private static final int limite_por_projeto = 10;
    private static final int limite_por_funcionario = 3;

    private final ProjetoRepository projetoRepository;
    private final MembroService membroService;

    public String execute(ReqAlocarMembroProjeto data){

        Projeto projeto = buscarProjeto(data.getIdProjeto());

        Membro membro = buscarFuncionario(data.getIdMembro());

        funcionarioJaAlocado(projeto, membro);

        projetosPorFuncionario(data.getIdMembro());

        validarQuantidadeProjeto(projeto);

        projeto.getFuncionarios().add(membro);

        projetoRepository.save(projeto);

        return String.format("Funcionario: %s alocado com sucesso ao projeto: %s", membro.getNome(), projeto.getNome());

    }

    private void projetosPorFuncionario(Long idFuncionario){
        List<Projeto> projetos = projetoRepository.findAllByFuncionariosId(idFuncionario).stream()
                .filter( r -> !(r.getStatusProjeto() == StatusProjeto.CANCELADO))
                .filter(r -> !(r.getStatusProjeto() == StatusProjeto.ENCERRADO))
                .collect(Collectors.toList());

        if (projetos.size() >= limite_por_funcionario)  throw new QuantidadePorFuncionarioException("Quantidade de projetos excedida para o funcionario: " + idFuncionario);

    }

    private Membro buscarFuncionario(Long id){
        return membroService.buscarFuncionario(id);
    }

    private void validarQuantidadeProjeto(Projeto projeto){
        if (projeto.getFuncionarios().size() >= limite_por_projeto){
            throw new ProjetoFuncionarioQuantidadeException("Projeto: " + projeto.getNome() + " com quantidade de funcionarios atingida");
        }
    }

    private void funcionarioJaAlocado(Projeto projeto, Membro membro){

        boolean alocado = projeto.getFuncionarios()
                .stream()
                .anyMatch( f -> Objects.equals(f.getId(), membro.getId()));

        if (alocado) throw new RuntimeException("Funcionario ja está no projeto");

    }

    private Projeto buscarProjeto(Long idProjeto){
        return projetoRepository.findById(idProjeto)
                .orElseThrow( () -> new EntityNotFoundException("Projeto não encontrado: " + idProjeto));
    }

}

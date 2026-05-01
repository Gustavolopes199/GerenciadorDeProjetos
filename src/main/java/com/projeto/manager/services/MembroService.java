package com.projeto.manager.services;

import com.projeto.manager.infra.exceptions.CargoNotFoundException;
import com.projeto.manager.infra.exceptions.MembroNaoFuncionarioException;
import com.projeto.manager.infra.exceptions.MembroNaoGerenteException;
import com.projeto.manager.infra.exceptions.MembroNotFoundException;
import com.projeto.manager.models.dtos.response.MockMembroResponse;
import com.projeto.manager.models.entity.Membro;
import com.projeto.manager.infra.repositorys.MembroRepository;
import com.projeto.manager.models.enums.Cargo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MembroService {

    private final MembroRepository repo;
    private final WebClient webClient;

    public Membro buscarMembro(Long id){
        return repo.findById(id).orElseGet( () -> buscaMembroMock(id));
    }

    public Membro buscarGerente(Long id){
        Membro entity = buscarMembro(id);
        if (entity.getCargo() != Cargo.GERENTE){
            throw new MembroNaoGerenteException(id.toString());
        }
        return entity;
    }

    public Membro buscarFuncionario(Long id){
        Membro entity = buscarMembro(id);
        if (entity.getCargo() != Cargo.FUNCIONARIO){
            throw new MembroNaoFuncionarioException(id.toString());
        }
        return entity;
    }

    public Membro criarAtualizarMembro(MockMembroResponse dto){
        Optional<Membro> entityOpt = repo.findById(dto.getId());

        if (entityOpt.isPresent()){
            Membro entity = entityOpt.get();

            entity.setNome(dto.getNome());
            entity.setCargo(buscarCargo(dto.getCargo()));

            repo.save(entity);

            return entity;
        }

        Membro entity = new Membro();
        entity.setId(dto.getId());
        entity.setNome(dto.getNome());
        entity.setCargo(buscarCargo(dto.getCargo()));

        return repo.save(entity);
    }

    public Membro buscaMembroMock(Long id){

        try {

            MockMembroResponse reponse = webClient.get()
                    .uri("/membro/{id}", id)
                    .retrieve()
                    .bodyToMono(MockMembroResponse.class)
                    .block();


            assert reponse != null;
            return criarAtualizarMembro(reponse);
        } catch (WebClientResponseException e) {
            throw new MembroNotFoundException("Membro não encontrado na api: " + id);
        }

    }

    private Cargo buscarCargo(String cargo){
        if (cargo.equalsIgnoreCase("GERENTE")) return Cargo.GERENTE;
        if (cargo.equalsIgnoreCase("FUNCIONARIO")) return Cargo.FUNCIONARIO;

        throw new CargoNotFoundException("Cargo não encontrado: " + cargo);
    }



}

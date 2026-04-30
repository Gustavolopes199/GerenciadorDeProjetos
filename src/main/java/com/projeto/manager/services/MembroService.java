package com.projeto.manager.services;

import com.projeto.manager.infra.exceptions.CargoNotFoundException;
import com.projeto.manager.infra.exceptions.MembroNaoGerenteException;
import com.projeto.manager.infra.mappers.MembroMapper;
import com.projeto.manager.infra.webclient.WebClientConfig;
import com.projeto.manager.models.dtos.request.CriarMembroDto;
import com.projeto.manager.models.dtos.response.MockMembroResponse;
import com.projeto.manager.models.entity.Membro;
import com.projeto.manager.infra.repositorys.MembroRepository;
import com.projeto.manager.models.enums.Cargo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MembroService {

    private final MembroRepository repo;
    private final MembroMapper mapper;
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

        MockMembroResponse reponse = webClient.get()
                .uri("/mock/membro/{}", id)
                .retrieve()
                .bodyToMono(MockMembroResponse.class)
                .block();

        assert reponse != null;
        return criarAtualizarMembro(reponse);

    }

    private Cargo buscarCargo(String cargo){
        if (cargo.equalsIgnoreCase("GERENTE")) return Cargo.GERENTE;
        if (cargo.equalsIgnoreCase("FUNCIONARIO")) return Cargo.FUNCIONARIO;

        throw new CargoNotFoundException("Cargo não encontrado: " + cargo);
    }



}

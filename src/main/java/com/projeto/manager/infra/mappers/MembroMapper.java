package com.projeto.manager.infra.mappers;

import com.projeto.manager.models.dtos.request.CriarMembroDto;
import com.projeto.manager.models.dtos.response.MockMembroResponse;
import com.projeto.manager.models.entity.Membro;
import org.springframework.stereotype.Component;

@Component
public class MembroMapper {


    public Membro dtoCriacaoToEntity(CriarMembroDto data){
        Membro entity = new Membro();
        entity.setNome(data.getNome());

        return entity;
    }

}

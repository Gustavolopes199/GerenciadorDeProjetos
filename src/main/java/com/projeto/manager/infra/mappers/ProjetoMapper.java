package com.projeto.manager.infra.mappers;

import com.projeto.manager.models.dtos.projeto.CadastroProjeto;
import com.projeto.manager.models.dtos.projeto.ResponseProjetoCadastrado;
import com.projeto.manager.models.entity.Projeto;
import org.springframework.stereotype.Component;

@Component
public class ProjetoMapper {

    public Projeto toEntity(CadastroProjeto data){
        Projeto entity = new Projeto();

        entity.setNome(data.getNome());
        entity.setDescricao(data.getDescricao());
        entity.setDataInicio(data.getDataInicio());
        entity.setDataTermino(data.getDataTermino());
        entity.setPrevisaoTermino(data.getPrevisaoTermino());

        return entity;
    }

    public ResponseProjetoCadastrado toDto(Projeto entity){
        ResponseProjetoCadastrado dto = new ResponseProjetoCadastrado();

        dto.setId(entity.getId());
        dto.setNome(entity.getNome());

        return dto;
    }

}

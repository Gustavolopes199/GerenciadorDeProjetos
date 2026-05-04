package com.projeto.manager.infra.mappers;

import com.projeto.manager.models.dtos.request.CadastroProjeto;
import com.projeto.manager.models.dtos.request.RequestUpdateProjeto;
import com.projeto.manager.models.dtos.response.ResponseProjetoCadastrado;
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
        entity.setOrcamentoTotal(data.getOrcamentoTotal());

        return entity;
    }

    public ResponseProjetoCadastrado toDto(Projeto entity){
        ResponseProjetoCadastrado dto = new ResponseProjetoCadastrado();

        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setOrcamentoTotal(entity.getOrcamentoTotal());
        dto.setDescricao(entity.getDescricao());
        dto.setGerente(entity.getGerente().getNome());

        return dto;
    }

    public Projeto updateToEntity(Projeto entity, RequestUpdateProjeto data){

        if (data.getStatusProjeto() != null){
            entity.setStatusProjeto(data.getStatusProjeto());
        }

        if (data.getDataInicio() != null){
            entity.setDataInicio(data.getDataInicio());
        }

        if (data.getOrcamentoTotal() != null){
            entity.setOrcamentoTotal(data.getOrcamentoTotal());
        }

        if (data.getDataTermino() != null){
            entity.setDataInicio(data.getDataTermino());
        }

        if (data.getNome() != null){
            entity.setNome(data.getNome());
        }

        if (data.getPrevisaoTermino() != null){
            entity.setPrevisaoTermino(data.getPrevisaoTermino());
        }

        return entity;
    }

}

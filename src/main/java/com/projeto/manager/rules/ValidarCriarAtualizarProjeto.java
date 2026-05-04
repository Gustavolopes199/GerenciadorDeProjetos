package com.projeto.manager.rules;

import com.projeto.manager.infra.exceptions.ErroValidarDadosException;
import com.projeto.manager.models.entity.Projeto;
import com.projeto.manager.models.enums.StatusProjeto;
import org.springframework.stereotype.Component;

@Component
public class ValidarCriarAtualizarProjeto {


    public void validar(Projeto entity){
        if (entity.getDataTermino() != null){
            if (entity.getDataInicio().isAfter(entity.getDataTermino())){
                throw new ErroValidarDadosException("Data incial não pode ser depois da data final");
            }
        }
        if (entity.getPrevisaoTermino() != null){
            if (entity.getDataInicio().isAfter(entity.getPrevisaoTermino())){
                throw new ErroValidarDadosException("Data incial não pode ser depois da data de previsão");
            }
        }
        if (entity.getStatusProjeto() == StatusProjeto.ENCERRADO){
            if (entity.getDataTermino() == null){
                throw new ErroValidarDadosException("So é possivel encerrar o projeto caso a data de termino também esteja preenchida");
            }
        }

    }

}

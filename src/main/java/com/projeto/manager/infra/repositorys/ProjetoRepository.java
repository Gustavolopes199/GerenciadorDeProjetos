package com.projeto.manager.infra.repositorys;

import com.projeto.manager.models.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Long>, JpaSpecificationExecutor<Projeto> {

    List<Projeto> findAllByFuncionariosId(Long membroId);

}

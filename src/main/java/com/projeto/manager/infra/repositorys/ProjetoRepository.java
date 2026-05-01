package com.projeto.manager.infra.repositorys;

import com.projeto.manager.models.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {

    List<Projeto> findAllByFuncionariosId(Long membroId);

}

package com.projeto.manager.infra.repositorys;

import com.projeto.manager.models.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}

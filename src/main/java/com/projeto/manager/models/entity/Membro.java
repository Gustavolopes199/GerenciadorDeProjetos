package com.projeto.manager.models.entity;


import com.projeto.manager.models.enums.Cargo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "membros")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Membro {

    @Id
    private Long id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private Cargo cargo;

}

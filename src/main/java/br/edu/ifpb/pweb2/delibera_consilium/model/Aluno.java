package br.edu.ifpb.pweb2.delibera_consilium.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String fone;
    private String matricula;
    private String login;
    private String senha;

    // Relacionamento inverso: Um aluno tem vários processos
    @OneToMany(mappedBy = "interessado")
    private List<Processo> processos;
}
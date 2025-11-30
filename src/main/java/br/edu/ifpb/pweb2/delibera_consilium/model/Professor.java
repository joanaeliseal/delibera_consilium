package br.edu.ifpb.pweb2.delibera_consilium.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String fone;
    private String matricula;
    private String login;
    private String senha;
    private boolean coordenador;

    // Processos onde este professor é o Relator
    @OneToMany(mappedBy = "relator")
    private List<Processo> processosRelator;

    // Colegiados dos quais ele é membro
    @ManyToMany(mappedBy = "membros")
    private List<Colegiado> colegiados;
}
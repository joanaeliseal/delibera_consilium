package br.edu.ifpb.pweb2.delibera_consilium.model;

import br.edu.ifpb.pweb2.delibera_consilium.validator.Matricula;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    private String fone;

    @NotBlank(message = "Campo obrigatório")
    @Matricula(message = "Matrícula deve conter apenas números (mínimo 8 dígitos)")
    @Column(unique = true)
    private String matricula;

    
    @NotBlank(message = "Login é obrigatório")
    private String login;

    private String senha;

    // Relacionamento inverso: Um aluno tem vários processos
    @OneToMany(mappedBy = "interessado")
    @ToString.Exclude
    private List<Processo> processos;
}
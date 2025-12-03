package br.edu.ifpb.pweb2.delibera_consilium.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank; 
import lombok.Data;
import lombok.ToString;
import java.util.List;

@Data
@Entity
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    private String fone;

    @NotBlank(message = "A matrícula é obrigatória")
    private String matricula;

    @NotBlank(message = "O login é obrigatório")
    private String login;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;
    
    private boolean coordenador;

    @OneToMany(mappedBy = "relator")
    @ToString.Exclude
    private List<Processo> processosRelator;

    @ManyToMany(mappedBy = "membros")
    @ToString.Exclude
    private List<Colegiado> colegiados;
}
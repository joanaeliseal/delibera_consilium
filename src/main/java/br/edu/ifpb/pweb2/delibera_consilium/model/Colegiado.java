package br.edu.ifpb.pweb2.delibera_consilium.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Colegiado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data de início é obrigatória")
    private LocalDate dataInicio;

    @NotNull(message = "A data de fim é obrigatória")
    private LocalDate dataFim;

    @NotBlank(message = "A descrição é obrigatória")
    private String descricao;

    @NotBlank(message = "A portaria é obrigatória")
    private String portaria;

    @NotBlank(message = "O nome do curso é obrigatório")
    private String curso;

    @ManyToMany
    @JoinTable(
        name = "colegiado_membros",
        joinColumns = @JoinColumn(name = "colegiado_id"),
        inverseJoinColumns = @JoinColumn(name = "professor_id")
    )
    @ToString.Exclude
    @NotEmpty(message = "O colegiado deve ter pelo menos um professor membro") 
    private List<Professor> membros;

    @OneToMany(mappedBy = "colegiado")
    @ToString.Exclude
    private List<Reuniao> reunioes;
}
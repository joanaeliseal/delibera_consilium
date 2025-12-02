package br.edu.ifpb.pweb2.delibera_consilium.model;

import jakarta.persistence.*;
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

    private LocalDate dataInicio;
    private LocalDate dataFim;
    private String descricao;
    private String portaria;
    private String curso;

    @ManyToMany
    @JoinTable(
        name = "colegiado_membros",
        joinColumns = @JoinColumn(name = "colegiado_id"),
        inverseJoinColumns = @JoinColumn(name = "professor_id")
    )
    @ToString.Exclude 
    private List<Professor> membros;

    @OneToMany(mappedBy = "colegiado")
    @ToString.Exclude 
    private List<Reuniao> reunioes;
}
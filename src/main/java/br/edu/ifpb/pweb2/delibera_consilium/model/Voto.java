package br.edu.ifpb.pweb2.delibera_consilium.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoVoto voto;

    private boolean ausente;

    @Column(columnDefinition = "TEXT")
    private String justificativa;

    @ManyToOne
    @JoinColumn(name = "processo_id")
    private Processo processo;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;
}
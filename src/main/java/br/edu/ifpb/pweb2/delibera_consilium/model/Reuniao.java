package br.edu.ifpb.pweb2.delibera_consilium.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Reuniao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataReuniao;

    @Enumerated(EnumType.STRING)
    private StatusReuniao status;

    @Lob 
    private byte[] ata;

    @ManyToOne
    @JoinColumn(name = "colegiado_id")
    private Colegiado colegiado;

    // Processos que estão na pauta desta reunião
    @OneToMany(mappedBy = "reuniao")
    private List<Processo> processosEmPauta;
}
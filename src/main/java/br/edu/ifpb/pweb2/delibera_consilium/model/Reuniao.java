package br.edu.ifpb.pweb2.delibera_consilium.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Reuniao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data da reuniao e obrigatoria")
    private LocalDate dataReuniao;

    @Enumerated(EnumType.STRING)
    private StatusReuniao status;

    // Momento em que a sessao foi iniciada
    private LocalDateTime dataHoraInicio;

    // Momento em que a sessao foi encerrada
    private LocalDateTime dataHoraFim;

    @Lob
    private byte[] ata;

    @NotNull(message = "Selecione um colegiado")
    @ManyToOne
    @JoinColumn(name = "colegiado_id")
    private Colegiado colegiado;

    // Processos que estao na pauta desta reuniao
    @OneToMany(mappedBy = "reuniao")
    private List<Processo> processosEmPauta;
}

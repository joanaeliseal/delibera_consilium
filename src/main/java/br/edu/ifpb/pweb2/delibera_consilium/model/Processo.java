package br.edu.ifpb.pweb2.delibera_consilium.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Processo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;
    private LocalDate dataRecepcao;
    private LocalDate dataDistribuicao;
    private LocalDate dataParecer;

    @Lob
    private byte[] parecer; // PDF ou Texto do parecer

    @Enumerated(EnumType.STRING)
    private TipoDecisao decisaoRelator;
    
    // Status do processo (CRIADO, DISTRIBUIDO, etc.)
    // Como você não listou um arquivo Enum separado para isso, deixei como String
    private String status; 

    // Quem abriu o processo (Aluno)
    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno interessado;

    // Assunto do processo
    @ManyToOne
    @JoinColumn(name = "assunto_id")
    private Assunto assunto;

    // Professor Relator (definido pelo Coordenador)
    @ManyToOne
    @JoinColumn(name = "relator_id")
    private Professor relator;

    // Reunião onde o processo foi pautado
    @ManyToOne
    @JoinColumn(name = "reuniao_id")
    private Reuniao reuniao;

    // Lista de votos dos membros
    @OneToMany(mappedBy = "processo")
    private List<Voto> votos;
}
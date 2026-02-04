package br.edu.ifpb.pweb2.delibera_consilium.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Processo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) 
    private String numero;

    @NotBlank(message = "O texto do requerimento é obrigatório")
    @Column(columnDefinition = "TEXT") 
    private String textoRequerimento;

    private LocalDate dataRecepcao;
    private LocalDate dataDistribuicao;
    private LocalDate dataParecer;

    @Lob
    private byte[] parecer;
    
    // NOVO: Campo para armazenar o PDF do requerimento
    @Lob
    @Column(columnDefinition = "bytea")
    private byte[] requerimentoPdf;
  
    private String requerimentoPdfNome;
    
    private String status; 

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno interessado;

    @NotNull(message = "Selecione um assunto para o processo")
    @ManyToOne
    @JoinColumn(name = "assunto_id")
    private Assunto assunto;

    @ManyToOne
    @JoinColumn(name = "relator_id")
    private Professor relator;

    @ManyToOne
    @JoinColumn(name = "reuniao_id")
    @ToString.Exclude
    private Reuniao reuniao;

    @OneToMany(mappedBy = "processo")
    @ToString.Exclude
    private List<Voto> votos;
    
    public boolean temRequerimentoPdf() {
        return this.requerimentoPdf != null && this.requerimentoPdf.length > 0;
    }
    
    public boolean podeReceberUpload() {
    // Só pode se o status for nulo (novo) ou "CRIADO"
    return this.status == null || "CRIADO".equals(this.status);
    }
}
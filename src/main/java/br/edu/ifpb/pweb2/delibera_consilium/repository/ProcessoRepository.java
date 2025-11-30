// Repositório Principal
package br.edu.ifpb.pweb2.delibera_consilium.repository;

import br.edu.ifpb.pweb2.delibera_consilium.model.Processo;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Long> {
    
    // REQFUNC 3: O professor consulta todos os processos que lhe foram designados
    List<Processo> findByRelator(Professor relator);

    // REQFUNC 2: O aluno consulta seus processos
    List<Processo> findByInteressado(Aluno interessado);
    
    // REQFUNC 7: Coordenador filtra por status (ex: "CRIADO", "DISTRIBUIDO")
    List<Processo> findByStatus(String status);
}
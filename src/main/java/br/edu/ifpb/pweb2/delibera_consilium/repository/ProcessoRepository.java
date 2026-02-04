// Repositório Principal
package br.edu.ifpb.pweb2.delibera_consilium.repository;

import br.edu.ifpb.pweb2.delibera_consilium.model.Processo;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProcessoRepository extends JpaRepository<Processo, Long> {
    
    // REQFUNC 3: O professor consulta todos os processos que lhe foram designados
    List<Processo> findByRelator(Professor relator);

    // REQFUNC 2: O aluno consulta seus processos
    List<Processo> findByInteressado(Aluno interessado);

    // Busca processos do aluno x que tenham o status y (REQFUNC 2)
    List<Processo> findByInteressadoAndStatus(Aluno interessado, String status);
    
    // REQFUNC 7: Coordenador filtra por status (ex: "CRIADO", "DISTRIBUIDO")
    List<Processo> findByStatus(String status);

    // REQFUNC 7
    @Query("SELECT p FROM Processo p WHERE " +
        "(:status IS NULL OR p.status = :status) AND " +
        "(:alunoId IS NULL OR p.interessado.id = :alunoId) AND " +
        "(:relatorId IS NULL OR p.relator.id = :relatorId)")
    List<Processo> findByFiltros(String status, Long alunoId, Long relatorId);

    // REQFUNC 2
    @Query("SELECT p FROM Processo p WHERE " +
           "p.interessado = :aluno AND " +
           "(:status IS NULL OR p.status = :status) AND " +
           "(:assuntoId IS NULL OR p.assunto.id = :assuntoId)")
    List<Processo> findByAlunoFiltros(Aluno aluno, String status, Long assuntoId, org.springframework.data.domain.Sort sort);

    // REQFUNC 9: Processos disponiveis para pauta (distribuidos sem reuniao)
    List<Processo> findByStatusAndReuniaoIsNull(String status);

    // Processos de uma reuniao especifica
    List<Processo> findByReuniaoId(Long reuniaoId);

    // Busca todos os processos de forma paginada
    Page<Processo> findAll(Pageable pageable);

    // Caso queira filtrar por status (ex: apenas processos "CRIADOS") de forma paginada
    Page<Processo> findByStatus(String status, Pageable pageable);

    @Query("SELECT p FROM Processo p WHERE " +
        "(:status IS NULL OR p.status = :status) AND " +
        "(:alunoId IS NULL OR p.interessado.id = :alunoId) AND " +
        "(:relatorId IS NULL OR p.relator.id = :relatorId)")
    Page<Processo> findByFiltrosPaginado(@Param("status") String status, 
                                        @Param("alunoId") Long alunoId, 
                                        @Param("relatorId") Long relatorId, 
                                        Pageable pageable);
}
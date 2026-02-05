package br.edu.ifpb.pweb2.delibera_consilium.repository;

import br.edu.ifpb.pweb2.delibera_consilium.model.Colegiado;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.model.Reuniao;
import br.edu.ifpb.pweb2.delibera_consilium.model.StatusReuniao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReuniaoRepository extends JpaRepository<Reuniao, Long> {

    List<Reuniao> findByStatus(StatusReuniao status);

    boolean existsByStatus(StatusReuniao status);

    List<Reuniao> findAllByOrderByDataReuniaoDesc();

    List<Reuniao> findByColegiado(Colegiado colegiado);

    List<Reuniao> findByColegiadoAndStatus(Colegiado colegiado, StatusReuniao status);

    @Query("SELECT r FROM Reuniao r WHERE " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(:colegiadoId IS NULL OR r.colegiado.id = :colegiadoId) " +
           "ORDER BY r.dataReuniao DESC")
    List<Reuniao> findByFiltros(@Param("status") StatusReuniao status,
                                 @Param("colegiadoId") Long colegiadoId);

    @Query("SELECT DISTINCT r FROM Reuniao r JOIN r.membros m WHERE m = :professor " +
           "AND (:status IS NULL OR r.status = :status) " +
           "ORDER BY r.dataReuniao DESC")
    List<Reuniao> findByMembro(@Param("professor") Professor professor,
                                @Param("status") StatusReuniao status);
}

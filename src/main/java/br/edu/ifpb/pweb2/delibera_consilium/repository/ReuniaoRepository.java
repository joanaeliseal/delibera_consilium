package br.edu.ifpb.pweb2.delibera_consilium.repository;

import br.edu.ifpb.pweb2.delibera_consilium.model.Reuniao;
import br.edu.ifpb.pweb2.delibera_consilium.model.StatusReuniao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReuniaoRepository extends JpaRepository<Reuniao, Long> {

    List<Reuniao> findByStatus(StatusReuniao status);

    List<Reuniao> findAllByOrderByDataReuniaoDesc();
}
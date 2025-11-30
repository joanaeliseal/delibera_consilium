package br.edu.ifpb.pweb2.delibera_consilium.repository;

import br.edu.ifpb.pweb2.delibera_consilium.model.Reuniao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReuniaoRepository extends JpaRepository<Reuniao, Long> {
}
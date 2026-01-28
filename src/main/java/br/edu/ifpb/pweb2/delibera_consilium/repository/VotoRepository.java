package br.edu.ifpb.pweb2.delibera_consilium.repository;

import br.edu.ifpb.pweb2.delibera_consilium.model.Processo;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

    List<Voto> findByProcesso(Processo processo);

    List<Voto> findByProcessoId(Long processoId);

    Optional<Voto> findByProcessoAndProfessor(Processo processo, Professor professor);

    boolean existsByProcessoAndProfessor(Processo processo, Professor professor);
}
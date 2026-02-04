package br.edu.ifpb.pweb2.delibera_consilium.repository;

import br.edu.ifpb.pweb2.delibera_consilium.model.Colegiado;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ColegiadoRepository extends JpaRepository<Colegiado, Long> {
    // Método para buscar um colegiado pelo seu coordenador
    Optional<Colegiado> findByCoordenador(Professor professor);
}
package br.edu.ifpb.pweb2.delibera_consilium.repository;

import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {
    //Buscar apenas professores que são coordenadores
    List<Professor> findByCoordenadorTrue();

    Professor findByLogin(String login);
}
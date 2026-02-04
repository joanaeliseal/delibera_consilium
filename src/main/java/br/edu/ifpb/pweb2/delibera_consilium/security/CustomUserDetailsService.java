package br.edu.ifpb.pweb2.delibera_consilium.security;

import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.repository.AlunoRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ProfessorRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ColegiadoRepository; // Adicionado
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private ColegiadoRepository colegiadoRepository; // Injetado para verificar coordenação

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // 1. Tenta buscar como Aluno
        Aluno aluno = alunoRepository.findByLogin(username);
        if (aluno != null) {
            return new User(
                aluno.getLogin(), 
                aluno.getSenha(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ALUNO"))
            );
        }

        // 2. Tenta buscar como Professor
        Professor professor = professorRepository.findByLogin(username);
        if (professor != null) {
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_PROFESSOR"));

            // Verifica se o professor é COORDENADOR de algum colegiado
            if (colegiadoRepository.findByCoordenador(professor).isPresent()) {
                authorities.add(new SimpleGrantedAuthority("ROLE_COORDENADOR"));
            }

            // Lógica para ADMIN: Se o login for "admin", adiciona a permissão
            if (username.equalsIgnoreCase("admin")) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }

            return new User(
                professor.getLogin(), 
                professor.getSenha(), 
                authorities
            );
        }

        throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }
}
package br.edu.ifpb.pweb2.delibera_consilium.security;

import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.repository.AlunoRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ColegiadoRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ProfessorRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AlunoRepository alunoRepository;
    private final ProfessorRepository professorRepository;
    private final ColegiadoRepository colegiadoRepository;

    public CustomUserDetailsService(AlunoRepository alunoRepository, 
                                    ProfessorRepository professorRepository, 
                                    ColegiadoRepository colegiadoRepository) {
        this.alunoRepository = alunoRepository;
        this.professorRepository = professorRepository;
        this.colegiadoRepository = colegiadoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // 1. Tenta buscar na tabela de Aluno
        Aluno aluno = alunoRepository.findByLogin(username);
        if (aluno != null) {
            return User.withUsername(aluno.getLogin())
                    .password(aluno.getSenha())
                    .roles("ALUNO")
                    .build();
        }

        // 2. Tenta buscar na tabela de Professor
        Professor professor = professorRepository.findByLogin(username);
        if (professor != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();

            // ⭐ LÓGICA DE SEPARAÇÃO DE PAPÉIS
            if ("admin".equals(professor.getLogin())) {
                // Se for o login 'admin', ele ganha APENAS a role de admin
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else {
                // Se for qualquer outro professor, ganha a role de professor
                authorities.add(new SimpleGrantedAuthority("ROLE_PROFESSOR"));

                // E se ele coordenar algum colegiado, ganha o "upgrade" para coordenador
                if (!colegiadoRepository.findByCoordenador(professor).isEmpty()) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_COORDENADOR"));
                }
            }

            return new User(professor.getLogin(), professor.getSenha(), authorities);
        }

        throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }
}
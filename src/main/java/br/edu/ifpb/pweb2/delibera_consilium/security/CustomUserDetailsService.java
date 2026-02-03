package br.edu.ifpb.pweb2.delibera_consilium.security;

import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.repository.AlunoRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Serviço customizado para autenticação de usuários
 * Busca alunos e professores no banco de dados
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    /**
     * Carrega o usuário pelo username (login)
     * Primeiro tenta encontrar como Aluno, depois como Professor
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        // Tenta buscar como Aluno primeiro
        Aluno aluno = alunoRepository.findByLogin(username);
        if (aluno != null) {
            return buildUserDetails(aluno.getLogin(), aluno.getSenha(), "ROLE_ALUNO");
        }
        
        // Se não encontrou como Aluno, tenta como Professor
        Professor professor = professorRepository.findByLogin(username);
        if (professor != null) {
            // Professor pode ter múltiplas roles
            List<String> roles = new ArrayList<>();
            roles.add("ROLE_PROFESSOR");
            
            // Se for coordenador, adiciona a role de coordenador também
            if (professor.isCoordenador()) {
                roles.add("ROLE_COORDENADOR");
            }
            
            return buildUserDetails(professor.getLogin(), professor.getSenha(), roles);
        }
        
        // Se não encontrou nem como Aluno nem como Professor
        throw new UsernameNotFoundException("Usuário não encontrado: " + username);
    }
    
    /**
     * Constrói o UserDetails com uma única role
     */
    private UserDetails buildUserDetails(String username, String password, String role) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        
        return User.builder()
                .username(username)
                .password(password)
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
    
    /**
     * Constrói o UserDetails com múltiplas roles
     */
    private UserDetails buildUserDetails(String username, String password, List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        
        return User.builder()
                .username(username)
                .password(password)
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
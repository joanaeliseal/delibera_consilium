package br.edu.ifpb.pweb2.delibera_consilium.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.repository.AlunoRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ProfessorRepository;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
// serviço de autenticação personalizado
    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Override
    public UserDetails carregarUsuarioByUsername(String username) throws UsernameNotFoundException {
    // método para carregar os detalhes do usuário (aluno ou professor) pelo nome de usuário (login)
        
        Aluno aluno = alunoRepository.findByLogin(username); // busca o aluno pelo login
        if (aluno != null) {
            return User.builder()
                    .username(aluno.getLogin())
                    .password(aluno.getSenha()) // senha verificada pelo BCrypt
                    .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_ALUNO"))) // papel do usuário
                    .build();
        }

        Professor professor = professorRepository.findByLogin(username); // busca o professor pelo login
        if (professor != null) { 
            // Professor pode ser coordenador ou não
            String role = professor.isCoordenador() ? "ROLE_COORDENADOR" : "ROLE_PROFESSOR";

            return User.builder()
                    .username(professor.getLogin())
                    .password(professor.getSenha()) // senha verificada pelo BCrypt
                    .authorities(Collections.singleton(new SimpleGrantedAuthority(role))) // papel do usuário
                    .build();
        }
        throw new UsernameNotFoundException("Usuário não encontrado: " + username); // nenhum usuário encontrado
    }
}

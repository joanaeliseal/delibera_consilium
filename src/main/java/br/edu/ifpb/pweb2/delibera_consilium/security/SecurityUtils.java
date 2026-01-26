package br.edu.ifpb.pweb2.delibera_consilium.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.repository.AlunoRepository;
import br.edu.ifpb.pweb2.delibera_consilium.repository.ProfessorRepository;
import org.springframework.security.core.context.SecurityContextHolder;


@Component
public class SecurityUtils {
// utilitários de segurança (obter usuário logado, verificar roles, etc.)
// será usada para substituir parâmetros alunoId/usuario

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    public String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return auth.getName();
        }
        return null;
    }

    public Aluno getAlunoLogado() {
        String username = getUsername();
        if (username != null) {
            return alunoRepository.findByLogin(username);
        }
        return null;
    }

    public Professor getProfessorLogado() {
        String username = getUsername();
        if (username != null) {
            return professorRepository.findByLogin(username);
        }
        return null;
    }

    public boolean hasRole(String role) {
    // verifica se o usuário logado possui a role especificada
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals(role));
        }

    }


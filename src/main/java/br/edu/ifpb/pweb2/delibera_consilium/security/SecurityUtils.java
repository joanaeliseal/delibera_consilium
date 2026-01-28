package br.edu.ifpb.pweb2.delibera_consilium.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Classe utilitária para obter informações do usuário logado
 */
public class SecurityUtils {

    /**
     * Obtém o username (login) do usuário atualmente autenticado
     * 
     * @return Username do usuário logado ou null se não estiver autenticado
     */
    public static String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        
        return principal.toString();
    }

    /**
     * Verifica se há um usuário autenticado
     * 
     * @return true se há usuário logado, false caso contrário
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * Verifica se o usuário logado tem uma role específica
     * 
     * @param role Nome da role (ex: "ROLE_ADMIN", "ROLE_ALUNO")
     * @return true se tem a role, false caso contrário
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(role));
    }

    /**
     * Verifica se o usuário é um ALUNO
     */
    public static boolean isAluno() {
        return hasRole("ROLE_ALUNO");
    }

    /**
     * Verifica se o usuário é um PROFESSOR
     */
    public static boolean isProfessor() {
        return hasRole("ROLE_PROFESSOR");
    }

    /**
     * Verifica se o usuário é um COORDENADOR
     */
    public static boolean isCoordenador() {
        return hasRole("ROLE_COORDENADOR");
    }

    /**
     * Verifica se o usuário é um ADMIN
     */
    public static boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }
}
package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.security.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    /**
     * Redireciona o usuário para a página apropriada baseado no seu papel
     */
    @GetMapping("/dashboard")
    public String dashboard() {
        
        // Verifica o papel do usuário e redireciona para a área apropriada
        if (SecurityUtils.isAdmin()) {
            return "redirect:/admin/alunos";
        }
        
        if (SecurityUtils.isCoordenador()) {
            return "redirect:/coord/processos";
        }
        
        if (SecurityUtils.isProfessor()) {
            return "redirect:/professor/processos";
        }
        
        if (SecurityUtils.isAluno()) {
            return "redirect:/aluno/processos";
        }
        
        // Fallback (não deveria acontecer)
        return "redirect:/login?error=true";
    }
}
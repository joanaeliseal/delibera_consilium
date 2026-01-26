package br.edu.ifpb.pweb2.delibera_consilium.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


// Controller para as páginas de login e home.

@Controller
public class LoginController {

    /**
     * Exibe a página de login.
     * Trata mensagens de erro, logout e sessão expirada.
     */
    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "expired", required = false) String expired,
            Model model) {
        
        if (error != null) {
            model.addAttribute("errorMsg", "Login ou senha incorretos. Tente novamente.");
        }
        
        if (logout != null) {
            model.addAttribute("msg", "Você saiu do sistema com sucesso.");
        }
        
        if (expired != null) {
            model.addAttribute("errorMsg", "Sua sessão expirou. Faça login novamente.");
        }
        
        return "login";
    }

    /**
     * Página inicial após login.
     * Redireciona para área específica baseada na ROLE do usuário.
     */
    @GetMapping({"/", "/home"})
    public String home() {
        // TODO: Implementar redirecionamento baseado em role
        // Por enquanto, apenas exibe página home
        return "home";
    }
}
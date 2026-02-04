package br.edu.ifpb.pweb2.delibera_consilium.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "Usuário ou senha inválidos!");
        }
        if (logout != null) {
            model.addAttribute("msg", "Você saiu do sistema com sucesso.");
        }
        return "login";
    }

    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "auth/acesso-negado"; 
    }
}
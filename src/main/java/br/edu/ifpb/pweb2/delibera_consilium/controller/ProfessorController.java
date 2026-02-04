package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProfessorService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/professores")
public class ProfessorController {

    private final ProfessorService service;
    private final PasswordEncoder passwordEncoder;

    public ProfessorController(ProfessorService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("professores", service.listarTodos());
        return "admin/professor/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("professor", new Professor());
        return "admin/professor/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("professor") Professor professor,
                         BindingResult result,
                         RedirectAttributes redirect) {

        if (result.hasErrors()) {
            return "admin/professor/form";
        }

        // ⭐ CRIPTOGRAFA A SENHA AUTOMATICAMENTE
        // Só criptografa se:
        // 1. For um novo professor (id == null) OU
        // 2. A senha foi alterada (não começa com $2a$ que é o prefixo do BCrypt)
        if (professor.getId() == null || 
            (professor.getSenha() != null && !professor.getSenha().isEmpty() && !professor.getSenha().startsWith("$2a$"))) {
            String senhaCriptografada = passwordEncoder.encode(professor.getSenha());
            professor.setSenha(senhaCriptografada);
        }

        service.salvar(professor);
        redirect.addFlashAttribute("msg", "Professor salvo com sucesso!");
        return "redirect:/admin/professores";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Professor professor = service.buscarPorId(id);
        // ⭐ IMPORTANTE: Limpa a senha ao editar para não mostrar o hash
        professor.setSenha("");
        model.addAttribute("professor", professor);
        return "admin/professor/form";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes redirect) {
        try {
            service.excluir(id);
            redirect.addFlashAttribute("msg", "Professor excluído com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMsg", "Erro: Este professor está vinculado a processos ou colegiados e não pode ser excluído.");
        }
        return "redirect:/admin/professores";
    }
}
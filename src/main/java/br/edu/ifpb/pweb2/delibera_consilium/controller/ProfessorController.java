package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProfessorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/professores")
public class ProfessorController {

    private final ProfessorService service;

    public ProfessorController(ProfessorService service) {
        this.service = service;
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

        service.salvar(professor);
        redirect.addFlashAttribute("msg", "Professor salvo com sucesso!");
        return "redirect:/admin/professores";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("professor", service.buscarPorId(id));
        return "admin/professor/form";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        service.excluir(id);
        redirect.addFlashAttribute("msg", "Professor excluído!");
        return "redirect:/admin/professores";
    }
}


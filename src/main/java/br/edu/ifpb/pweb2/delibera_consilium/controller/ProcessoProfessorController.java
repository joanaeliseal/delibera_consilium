package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.security.SecurityUtils;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProcessoService;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProfessorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/professor/processos")
public class ProcessoProfessorController {

    private final ProcessoService processoService;
    private final ProfessorService professorService;

    public ProcessoProfessorController(ProcessoService processoService, ProfessorService professorService) {
        this.processoService = processoService;
        this.professorService = professorService;
    }

    @GetMapping
    public String listarAtribuidos(Model model, RedirectAttributes redirect) {

        String username = SecurityUtils.getAuthenticatedUsername();
        Professor professorLogado = professorService.buscarPorLogin(username);

        if (professorLogado == null) {
            redirect.addFlashAttribute("errorMsg", "Erro: Usuário não autenticado!");
            return "redirect:/login";
        }

        model.addAttribute("processos", processoService.listarPorRelator(professorLogado));
        model.addAttribute("professorLogado", professorLogado);

        return "professor/processo/list";
    }
}
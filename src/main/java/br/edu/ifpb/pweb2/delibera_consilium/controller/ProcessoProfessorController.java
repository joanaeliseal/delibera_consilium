package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProcessoService;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProfessorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    // O param usuario é opcional, se não for informado na URL, ele assume que o id é 1
    public String listarAtribuidos(@RequestParam(required = false, defaultValue = "1") Long usuario, Model model) {
        
        Professor professorLogado = professorService.buscarPorId(usuario);

        if (professorLogado == null) {
            model.addAttribute("processos", null);
        } else {
            // REQFUNC 3: busca apenas processos onde ele é o relator
            model.addAttribute("processos", processoService.listarPorRelator(professorLogado));
            model.addAttribute("professorLogado", professorLogado); 
        }
        
        return "professor/processo/list";
    }
}
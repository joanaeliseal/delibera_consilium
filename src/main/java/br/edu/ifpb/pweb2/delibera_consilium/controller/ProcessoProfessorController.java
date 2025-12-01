package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProcessoService;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProfessorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String listarAtribuidos(Model model) {
        Professor professorLogado = this.getProfessorLogado();

        // REQFUNC 3
        model.addAttribute("processos", processoService.listarPorRelator(professorLogado));
        
        return "professor/processo/list";
    }

    // metodo fake pra simular login 
    private Professor getProfessorLogado() {
        return professorService.buscarPorId(1L); 
    }
}
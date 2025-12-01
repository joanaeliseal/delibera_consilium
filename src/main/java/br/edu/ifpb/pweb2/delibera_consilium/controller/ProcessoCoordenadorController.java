package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.service.ProcessoService;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProfessorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/coord/processos")
public class ProcessoCoordenadorController {

    private final ProcessoService processoService;
    private final ProfessorService professorService;

    public ProcessoCoordenadorController(ProcessoService processoService, ProfessorService professorService) {
        this.processoService = processoService;
        this.professorService = professorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("processos", processoService.listarTodos());
        model.addAttribute("professores", professorService.listarTodos());
        return "coord/processo/list";
    }

    @PostMapping("/distribuir/{id}")
    public String distribuir(@PathVariable Long id,
                             @RequestParam Long professorId) {

        processoService.distribuirProcesso(id, professorId);
        return "redirect:/coord/processos";
    }
}

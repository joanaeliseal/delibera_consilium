package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.service.AlunoService; 
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
    private final AlunoService alunoService; 

    public ProcessoCoordenadorController(ProcessoService processoService, 
                                         ProfessorService professorService,
                                         AlunoService alunoService) {
        this.processoService = processoService;
        this.professorService = professorService;
        this.alunoService = alunoService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String status,
                         @RequestParam(required = false) Long alunoId,
                         @RequestParam(required = false) Long relatorId,
                         Model model) {
        
        // REQFUNC 7
        model.addAttribute("processos", processoService.listarComFiltros(status, alunoId, relatorId));
        
        model.addAttribute("professores", professorService.listarTodos());
        model.addAttribute("alunos", alunoService.listarTodos());
        
        return "coord/processo/list";
    }

    @PostMapping("/distribuir/{id}")
    public String distribuir(@PathVariable Long id, @RequestParam Long professorId) {
        // REQFUNC 8: distribuir processo
        processoService.distribuirProcesso(id, professorId);
        return "redirect:/coord/processos";
    }
}
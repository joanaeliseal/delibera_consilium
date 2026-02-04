package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Processo;
import br.edu.ifpb.pweb2.delibera_consilium.service.AlunoService; 
import br.edu.ifpb.pweb2.delibera_consilium.service.ProcessoService;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProfessorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
                         @PageableDefault(size = 5, sort = "id") Pageable pageable, // Adicionado para REQNAOFUNC 9
                         Model model) {
        
        // Chamada ao service retornando um Page (importante para o reflexo no banco)
        Page<Processo> page = processoService.listarComFiltrosPaginado(status, alunoId, relatorId, pageable);
        
        model.addAttribute("page", page); // Enviamos o objeto de página
        model.addAttribute("professores", professorService.listarTodos());
        model.addAttribute("alunos", alunoService.listarTodos());
        
        return "coord/processo/list";
    }

    @PostMapping("/distribuir/{id}")
    public String distribuir(@PathVariable Long id, @RequestParam Long professorId) {
        processoService.distribuirProcesso(id, professorId);
        return "redirect:/coord/processos";
    }
}
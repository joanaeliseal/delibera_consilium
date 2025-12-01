package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Processo;
import br.edu.ifpb.pweb2.delibera_consilium.service.AssuntoService;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProcessoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/aluno/processos")
public class ProcessoAlunoController {

    private final ProcessoService processoService;
    private final AssuntoService assuntoService;

    public ProcessoAlunoController(ProcessoService processoService, AssuntoService assuntoService) {
        this.processoService = processoService;
        this.assuntoService = assuntoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("processos", processoService.listarTodos());
        return "aluno/processo/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("processo", new Processo());
        model.addAttribute("assuntos", assuntoService.listarTodos());
        return "aluno/processo/form";
    }

    @PostMapping("/salvar")
    public String salvar(Processo processo) {
        processoService.salvar(processo);
        return "redirect:/aluno/processos";
    }
}
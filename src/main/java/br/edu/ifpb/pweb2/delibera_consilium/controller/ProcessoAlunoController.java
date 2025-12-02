package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;
import br.edu.ifpb.pweb2.delibera_consilium.model.Processo;
import br.edu.ifpb.pweb2.delibera_consilium.service.AlunoService;
import br.edu.ifpb.pweb2.delibera_consilium.service.AssuntoService;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProcessoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/aluno/processos")
public class ProcessoAlunoController {

    private final ProcessoService processoService;
    private final AssuntoService assuntoService;
    private final AlunoService alunoService;

    public ProcessoAlunoController(ProcessoService processoService, AssuntoService assuntoService, AlunoService alunoService) {
        this.processoService = processoService;
        this.assuntoService = assuntoService;
        this.alunoService = alunoService;
    }

    @GetMapping
    public String listar(@RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "alunoId", required = false, defaultValue = "1") Long alunoId,
                         Model model) {
        
        // busca o aluno pelo ID da URL ( 1 se não passar nada)
        Aluno alunoLogado = alunoService.buscarPorId(alunoId);

        if (alunoLogado != null) {
            // lista os processos desse aluno 
            model.addAttribute("processos", processoService.listarPorInteressado(alunoLogado, status));
            model.addAttribute("alunoId", alunoId); 
        }
        
        return "aluno/processo/list";
    }

    @GetMapping("/novo")
    public String novo(@RequestParam(value = "alunoId", required = false, defaultValue = "1") Long alunoId, 
                       Model model) {
        
        Processo p = new Processo();
        
        Aluno aluno = alunoService.buscarPorId(alunoId);
        p.setInteressado(aluno); 
        
        model.addAttribute("processo", p);
        model.addAttribute("assuntos", assuntoService.listarTodos());
        model.addAttribute("alunoId", alunoId); 
        
        return "aluno/processo/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Processo processo, 
                         BindingResult result, 
                         @RequestParam("alunoId") Long alunoId, 
                         Model model, 
                         RedirectAttributes redirect) {
        
        if (result.hasErrors()) {
            model.addAttribute("assuntos", assuntoService.listarTodos());
            model.addAttribute("alunoId", alunoId); 
            return "aluno/processo/form";
        }

        // garante que o processo pertença ao aluno do id informado
        Aluno aluno = alunoService.buscarPorId(alunoId);
        processo.setInteressado(aluno);
        
        processoService.salvar(processo);
        
        redirect.addFlashAttribute("msg", "Processo criado com sucesso!");
        return "redirect:/aluno/processos?alunoId=" + alunoId;
    }
}
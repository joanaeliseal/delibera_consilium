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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam; 
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
    public String listar(@RequestParam(value = "status", required = false) String status, Model model) {
        // busca o aluno logado (login fake id 1)
        Aluno alunoLogado = this.getAlunoLogado();

        // passa o status (pode ser null) pro serviço filtrar
        model.addAttribute("processos", processoService.listarPorInteressado(alunoLogado, status));
        
        return "aluno/processo/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("processo", new Processo());
        model.addAttribute("assuntos", assuntoService.listarTodos());
        return "aluno/processo/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Processo processo, BindingResult result, Model model, RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("assuntos", assuntoService.listarTodos());
            return "aluno/processo/form";
        }
        processo.setInteressado(this.getAlunoLogado());
        processoService.salvar(processo);
        
        redirect.addFlashAttribute("msg", "Processo criado com sucesso!");
        return "redirect:/aluno/processos";
    }

    private Aluno getAlunoLogado() {
        //garante que existe um aluno com id 1 no banco!
        return alunoService.buscarPorId(1L); 
    }
}
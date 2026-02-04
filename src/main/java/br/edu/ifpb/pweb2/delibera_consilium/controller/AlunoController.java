package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;
import br.edu.ifpb.pweb2.delibera_consilium.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/alunos")
public class AlunoController {

    private final AlunoService service;

    public AlunoController(AlunoService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alunos", service.listarTodos());
        return "admin/aluno/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("aluno", new Aluno());
        return "admin/aluno/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("aluno") Aluno aluno,
                         BindingResult result,
                         RedirectAttributes redirect) {
       
        // busca no banco se já tem alguém com essa matrícula
        Aluno alunoExistente = service.buscarPorMatricula(aluno.getMatricula());

        // se encontrou alguem e nao sou eu mesmo (pros casos de editar)
        if (alunoExistente != null && !alunoExistente.getId().equals(aluno.getId())) {
            result.rejectValue("matricula", "error.matricula", "Esta matrícula já está cadastrada para outro aluno.");
        }

        if (result.hasErrors()) {
            return "admin/aluno/form";
        }

        service.salvar(aluno);
        redirect.addFlashAttribute("msg", "Aluno salvo com sucesso!");
        return "redirect:/admin/alunos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("aluno", service.buscarPorId(id));
        return "admin/aluno/form";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes redirect) {
        try {
            service.excluir(id);
            redirect.addFlashAttribute("msg", "Aluno excluído com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMsg", "Não é possível excluir este aluno pois existem processos vinculados a ele.");
        }
        return "redirect:/admin/alunos";
    }
}

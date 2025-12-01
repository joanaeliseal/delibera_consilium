package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Colegiado;
import br.edu.ifpb.pweb2.delibera_consilium.service.ColegiadoService;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProfessorService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/colegiados")
public class ColegiadoController {

    private final ColegiadoService service;
    private final ProfessorService professorService;

    public ColegiadoController(ColegiadoService service, ProfessorService professorService) {
        this.service = service;
        this.professorService = professorService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("colegiados", service.listarTodos());
        return "admin/colegiado/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("colegiado", new Colegiado());
        model.addAttribute("professores", professorService.listarTodos());
        return "admin/colegiado/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("colegiado") Colegiado colegiado,
                         BindingResult result,
                         RedirectAttributes redirect,
                         Model model) {

        if (result.hasErrors()) {
            model.addAttribute("professores", professorService.listarTodos());
            return "admin/colegiado/form";
        }

        service.salvar(colegiado);
        redirect.addFlashAttribute("msg", "Colegiado salvo com sucesso!");
        return "redirect:/admin/colegiados";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("colegiado", service.buscarPorId(id));
        model.addAttribute("professores", professorService.listarTodos());
        return "admin/colegiado/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        service.excluir(id);
        redirect.addFlashAttribute("msg", "Colegiado excluído!");
        return "redirect:/admin/colegiados";
    }
}


package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Assunto;
import br.edu.ifpb.pweb2.delibera_consilium.service.AssuntoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/assuntos")
public class AssuntoController {

    private final AssuntoService service;

    public AssuntoController(AssuntoService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("assuntos", service.listarTodos());
        return "admin/assunto/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("assunto", new Assunto());
        return "admin/assunto/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("assunto") Assunto assunto,
                         BindingResult result,
                         RedirectAttributes redirect) {

        if (result.hasErrors()) {
            return "admin/assunto/form";
        }

        service.salvar(assunto);
        redirect.addFlashAttribute("msg", "Assunto salvo com sucesso!");
        return "redirect:/admin/assuntos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("assunto", service.buscarPorId(id));
        return "admin/assunto/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        service.excluir(id);
        redirect.addFlashAttribute("msg", "Assunto removido!");
        return "redirect:/admin/assuntos";
    }
}

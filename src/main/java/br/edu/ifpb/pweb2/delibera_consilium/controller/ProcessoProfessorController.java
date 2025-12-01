package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.service.ProcessoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/professor/processos")
public class ProcessoProfessorController {

    private final ProcessoService service;

    public ProcessoProfessorController(ProcessoService service) {
        this.service = service;
    }

    @GetMapping
    public String listarAtribuidos(Model model) {
        model.addAttribute("processos", service.listarTodos());
        return "professor/processo/list";
    }
}
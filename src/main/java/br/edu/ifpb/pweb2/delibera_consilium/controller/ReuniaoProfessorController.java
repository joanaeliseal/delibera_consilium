package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.StatusReuniao;
import br.edu.ifpb.pweb2.delibera_consilium.service.ReuniaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/professor/reunioes")
public class ReuniaoProfessorController {

    private final ReuniaoService reuniaoService;

    public ReuniaoProfessorController(ReuniaoService reuniaoService) {
        this.reuniaoService = reuniaoService;
    }

    @GetMapping
    public String listarReunioes(
            @RequestParam(required = false) String status,
            Model model) {

        StatusReuniao statusFiltro = null;

        if (status != null && !status.isEmpty()) {
            try {
                statusFiltro = StatusReuniao.valueOf(status);
            } catch (IllegalArgumentException e) {
                // status invalido, ignora o filtro
            }
        }

        model.addAttribute("reunioes", reuniaoService.listarComFiltro(statusFiltro));
        model.addAttribute("statusSelecionado", status);
        model.addAttribute("statusList", StatusReuniao.values());

        return "professor/reuniao/list";
    }
}

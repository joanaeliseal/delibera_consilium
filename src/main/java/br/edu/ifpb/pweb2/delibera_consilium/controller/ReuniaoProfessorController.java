package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.model.StatusReuniao;
import br.edu.ifpb.pweb2.delibera_consilium.security.SecurityUtils;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProfessorService;
import br.edu.ifpb.pweb2.delibera_consilium.service.ReuniaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
 
@Controller
@RequestMapping("/professor/reunioes")
public class ReuniaoProfessorController {

    private final ReuniaoService reuniaoService;
    private final ProfessorService professorService;

    public ReuniaoProfessorController(ReuniaoService reuniaoService, ProfessorService professorService) {
        this.reuniaoService = reuniaoService;
        this.professorService = professorService;
    }

    @GetMapping
    public String listarReunioes(
            @RequestParam(required = false) String status,
            Model model,
            RedirectAttributes redirect) {

        String username = SecurityUtils.getAuthenticatedUsername();
        Professor professor = professorService.buscarPorLogin(username);

        if (professor == null) {
            redirect.addFlashAttribute("errorMsg", "Erro: Usuário não autenticado!");
            return "redirect:/login";
        }

        StatusReuniao statusFiltro = null;

        if (status != null && !status.isEmpty()) {
            try {
                statusFiltro = StatusReuniao.valueOf(status);
            } catch (IllegalArgumentException e) {
                // status invalido, ignora o filtro
            }
        }

        model.addAttribute("reunioes", reuniaoService.listarPorMembro(professor, statusFiltro));
        model.addAttribute("statusSelecionado", status);
        model.addAttribute("statusList", StatusReuniao.values());

        return "professor/reuniao/list";
    }
}

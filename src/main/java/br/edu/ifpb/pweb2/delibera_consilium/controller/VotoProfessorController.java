package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Processo;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.model.TipoDecisao;
import br.edu.ifpb.pweb2.delibera_consilium.model.TipoVoto;
import br.edu.ifpb.pweb2.delibera_consilium.model.Voto;
import br.edu.ifpb.pweb2.delibera_consilium.security.SecurityUtils;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProcessoService;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProfessorService;
import br.edu.ifpb.pweb2.delibera_consilium.service.VotoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/professor/votos")
public class VotoProfessorController {

    private final VotoService votoService;
    private final ProcessoService processoService;
    private final ProfessorService professorService;

    public VotoProfessorController(VotoService votoService, ProcessoService processoService, ProfessorService professorService) {
        this.votoService = votoService;
        this.processoService = processoService;
        this.professorService = professorService;
    }

    @GetMapping("/processo/{processoId}")
    public String formVotar(
            @PathVariable Long processoId,
            Model model,
            RedirectAttributes redirect) {

        Processo processo = processoService.buscarPorId(processoId);

        String username = SecurityUtils.getAuthenticatedUsername();
        Professor professor = professorService.buscarPorLogin(username);

        if (processo == null) {
            redirect.addFlashAttribute("errorMsg", "Processo não encontrado!");
            return "redirect:/professor/processos";
        }

        if (professor == null) {
            redirect.addFlashAttribute("errorMsg", "Erro: Usuário não autenticado!");
            return "redirect:/login";
        }

        Optional<Voto> votoExistente = votoService.buscarVoto(processo, professor);

        // Verifica se o professor logado é o relator do processo
        boolean isRelator = processo.getRelator() != null &&
                processo.getRelator().getId().equals(professor.getId());

        model.addAttribute("processo", processo);
        model.addAttribute("professor", professor);
        model.addAttribute("votoExistente", votoExistente.orElse(null));
        model.addAttribute("tiposVoto", TipoVoto.values());
        model.addAttribute("tiposDecisao", TipoDecisao.values());
        model.addAttribute("votos", votoService.listarPorProcesso(processoId));
        model.addAttribute("isRelator", isRelator);

        return "professor/voto/form";
    }

    @PostMapping("/registrar")
    public String registrarVoto(
            @RequestParam Long processoId,
            @RequestParam TipoVoto tipoVoto,
            @RequestParam(required = false) String justificativa,
            RedirectAttributes redirect) {

        String username = SecurityUtils.getAuthenticatedUsername();
        Professor professor = professorService.buscarPorLogin(username);

        if (professor == null) {
            redirect.addFlashAttribute("errorMsg", "Erro: Usuário não autenticado!");
            return "redirect:/login";
        }

        try {
            votoService.registrarVoto(processoId, professor.getId(), tipoVoto, justificativa);
            redirect.addFlashAttribute("msg", "Voto registrado com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMsg", "Erro ao registrar voto: " + e.getMessage());
        }

        return "redirect:/professor/votos/processo/" + processoId;
    }

    @PostMapping("/ausencia")
    public String registrarAusencia(
            @RequestParam Long processoId,
            RedirectAttributes redirect) {

        String username = SecurityUtils.getAuthenticatedUsername();
        Professor professor = professorService.buscarPorLogin(username);

        if (professor == null) {
            redirect.addFlashAttribute("errorMsg", "Erro: Usuário não autenticado!");
            return "redirect:/login";
        }

        try {
            votoService.registrarAusencia(processoId, professor.getId());
            redirect.addFlashAttribute("msg", "Ausencia registrada com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMsg", "Erro ao registrar ausencia: " + e.getMessage());
        }

        return "redirect:/professor/votos/processo/" + processoId;
    }

    /**
     * Registra a decisão do relator (DEFERIMENTO ou INDEFERIMENTO)
     * Apenas o relator do processo pode usar este endpoint
     */
    @PostMapping("/decisao-relator")
    public String registrarDecisaoRelator(
            @RequestParam Long processoId,
            @RequestParam TipoDecisao decisao,
            RedirectAttributes redirect) {

        String username = SecurityUtils.getAuthenticatedUsername();
        Professor professor = professorService.buscarPorLogin(username);

        if (professor == null) {
            redirect.addFlashAttribute("errorMsg", "Erro: Usuário não autenticado!");
            return "redirect:/login";
        }

        Processo processo = processoService.buscarPorId(processoId);
        if (processo == null) {
            redirect.addFlashAttribute("errorMsg", "Processo não encontrado!");
            return "redirect:/professor/processos";
        }

        // Verifica se o professor logado é o relator
        if (processo.getRelator() == null || !processo.getRelator().getId().equals(professor.getId())) {
            redirect.addFlashAttribute("errorMsg", "Apenas o relator pode registrar a decisão!");
            return "redirect:/professor/votos/processo/" + processoId;
        }

        try {
            processoService.registrarDecisaoRelator(processoId, decisao);
            redirect.addFlashAttribute("msg", "Decisão do relator registrada: " + decisao);
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMsg", "Erro ao registrar decisão: " + e.getMessage());
        }

        return "redirect:/professor/votos/processo/" + processoId;
    }
}

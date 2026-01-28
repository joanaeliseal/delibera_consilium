package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Processo;
import br.edu.ifpb.pweb2.delibera_consilium.model.Professor;
import br.edu.ifpb.pweb2.delibera_consilium.model.TipoVoto;
import br.edu.ifpb.pweb2.delibera_consilium.model.Voto;
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
            @RequestParam(required = false, defaultValue = "1") Long usuario,
            Model model) {

        Processo processo = processoService.buscarPorId(processoId);
        Professor professor = professorService.buscarPorId(usuario);

        if (processo == null) {
            return "redirect:/professor/processos?error=Processo+nao+encontrado";
        }

        // Verifica se ja existe um voto deste professor neste processo
        Optional<Voto> votoExistente = Optional.empty();
        if (professor != null) {
            votoExistente = votoService.buscarVoto(processo, professor);
        }

        model.addAttribute("processo", processo);
        model.addAttribute("professor", professor);
        model.addAttribute("votoExistente", votoExistente.orElse(null));
        model.addAttribute("tiposVoto", TipoVoto.values());
        model.addAttribute("votos", votoService.listarPorProcesso(processoId));

        return "professor/voto/form";
    }

    @PostMapping("/registrar")
    public String registrarVoto(
            @RequestParam Long processoId,
            @RequestParam Long professorId,
            @RequestParam TipoVoto tipoVoto,
            @RequestParam(required = false) String justificativa,
            RedirectAttributes redirect) {

        try {
            votoService.registrarVoto(processoId, professorId, tipoVoto, justificativa);
            redirect.addFlashAttribute("msg", "Voto registrado com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMsg", "Erro ao registrar voto: " + e.getMessage());
        }

        return "redirect:/professor/votos/processo/" + processoId + "?usuario=" + professorId;
    }

    @PostMapping("/ausencia")
    public String registrarAusencia(
            @RequestParam Long processoId,
            @RequestParam Long professorId,
            RedirectAttributes redirect) {

        try {
            votoService.registrarAusencia(processoId, professorId);
            redirect.addFlashAttribute("msg", "Ausencia registrada com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMsg", "Erro ao registrar ausencia: " + e.getMessage());
        }

        return "redirect:/professor/votos/processo/" + processoId + "?usuario=" + professorId;
    }
}

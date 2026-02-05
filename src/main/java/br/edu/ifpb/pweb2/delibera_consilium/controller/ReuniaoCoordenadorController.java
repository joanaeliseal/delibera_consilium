package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Processo;
import br.edu.ifpb.pweb2.delibera_consilium.model.Reuniao;
import br.edu.ifpb.pweb2.delibera_consilium.model.StatusReuniao;
import br.edu.ifpb.pweb2.delibera_consilium.service.ColegiadoService;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProcessoService;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProfessorService;
import br.edu.ifpb.pweb2.delibera_consilium.service.ReuniaoService;

import java.util.HashMap;
import java.util.Map;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/coord/reunioes")
public class ReuniaoCoordenadorController {

    private final ReuniaoService reuniaoService;
    private final ColegiadoService colegiadoService;
    private final ProcessoService processoService;
    private final ProfessorService professorService;

    public ReuniaoCoordenadorController(ReuniaoService reuniaoService,
                                         ColegiadoService colegiadoService,
                                         ProcessoService processoService,
                                         ProfessorService professorService) {
        this.reuniaoService = reuniaoService;
        this.colegiadoService = colegiadoService;
        this.processoService = processoService;
        this.professorService = professorService;
    }

    /**
     * Lista todas as reunioes com filtros opcionais
     */
    @GetMapping
    public String listar(@RequestParam(required = false) String status,
                         @RequestParam(required = false) Long colegiadoId,
                         Model model) {

        StatusReuniao statusFiltro = null;
        if (status != null && !status.isEmpty()) {
            try {
                statusFiltro = StatusReuniao.valueOf(status);
            } catch (IllegalArgumentException e) {
                // Status invalido, ignora filtro
            }
        }

        model.addAttribute("reunioes", reuniaoService.listarComFiltros(statusFiltro, colegiadoId));
        model.addAttribute("colegiados", colegiadoService.listarTodos());
        model.addAttribute("statusList", StatusReuniao.values());
        model.addAttribute("statusSelecionado", status);
        model.addAttribute("colegiadoSelecionado", colegiadoId);

        return "coord/reuniao/list";
    }

    /**
     * Formulario para criar nova reuniao
     */
    @GetMapping("/nova")
    public String nova(Model model) {
        model.addAttribute("reuniao", new Reuniao());
        model.addAttribute("colegiados", colegiadoService.listarTodos());
        model.addAttribute("professores", professorService.listarTodos());
        return "coord/reuniao/form";
    }

    /**
     * Formulario para editar reuniao existente
     */
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Reuniao reuniao = reuniaoService.buscarPorId(id);
        if (reuniao == null) {
            return "redirect:/coord/reunioes";
        }
        model.addAttribute("reuniao", reuniao);
        model.addAttribute("colegiados", colegiadoService.listarTodos());
        model.addAttribute("professores", professorService.listarTodos());
        return "coord/reuniao/form";
    }

    /**
     * Salva uma nova reuniao ou atualiza existente
     */
    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("reuniao") Reuniao reuniao,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirect) {

        if (result.hasErrors()) {
            model.addAttribute("colegiados", colegiadoService.listarTodos());
            model.addAttribute("professores", professorService.listarTodos());
            return "coord/reuniao/form";
        }

        if (reuniao.getId() == null) {
            reuniaoService.criarSessao(reuniao);
            redirect.addFlashAttribute("msg", "Reunião criada com sucesso!");
        } else {
            reuniaoService.salvar(reuniao);
            redirect.addFlashAttribute("msg", "Reunião atualizada com sucesso!");
        }

        return "redirect:/coord/reunioes";
    }

    /**
     * Exclui uma reuniao (apenas se estiver PROGRAMADA)
     */
    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            reuniaoService.excluir(id);
            redirect.addFlashAttribute("msg", "Reunião excluída com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMsg", "Erro ao excluir reunião: " + e.getMessage());
        }
        return "redirect:/coord/reunioes";
    }

    /**
     * Pagina para gerenciar a pauta da reuniao
     */
    @GetMapping("/{id}/pauta")
    public String gerenciarPauta(@PathVariable Long id, Model model) {
        Reuniao reuniao = reuniaoService.buscarPorId(id);
        if (reuniao == null) {
            return "redirect:/coord/reunioes";
        }

        model.addAttribute("reuniao", reuniao);
        model.addAttribute("processosDisponiveis", reuniaoService.listarProcessosDisponiveis());

        return "coord/reuniao/pauta";
    }

    /**
     * Adiciona um processo a pauta da reuniao
     */
    @PostMapping("/{id}/pauta/adicionar")
    public String adicionarProcesso(@PathVariable Long id,
                                    @RequestParam Long processoId,
                                    RedirectAttributes redirect) {
        reuniaoService.adicionarProcessoAPauta(id, processoId);
        redirect.addFlashAttribute("msg", "Processo adicionado a pauta!");
        return "redirect:/coord/reunioes/" + id + "/pauta";
    }

    /**
     * Remove um processo da pauta da reuniao
     */
    @PostMapping("/{id}/pauta/remover/{processoId}")
    public String removerProcesso(@PathVariable Long id,
                                   @PathVariable Long processoId,
                                   RedirectAttributes redirect) {
        reuniaoService.removerProcessoDaPauta(id, processoId);
        redirect.addFlashAttribute("msg", "Processo removido da pauta!");
        return "redirect:/coord/reunioes/" + id + "/pauta";
    }

    /**
     * Inicia a sessao (REQFUNC 10)
     */
    @PostMapping("/{id}/iniciar")
    public String iniciarSessao(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            Reuniao reuniao = reuniaoService.iniciarSessao(id);
            if (reuniao != null && reuniao.getStatus() == StatusReuniao.EM_ANDAMENTO) {
                redirect.addFlashAttribute("msg", "Sessão iniciada com sucesso!");
                return "redirect:/coord/reunioes/" + id + "/conduzir";
            }
            redirect.addFlashAttribute("errorMsg", "Não foi possível iniciar a sessão.");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMsg", e.getMessage());
        }
        return "redirect:/coord/reunioes";
    }

    /**
     * Pagina de conducao da sessao (REQFUNC 11)
     */
    @GetMapping("/{id}/conduzir")
    public String conduzirSessao(@PathVariable Long id, Model model) {
        Reuniao reuniao = reuniaoService.buscarPorId(id);
        if (reuniao == null || reuniao.getStatus() != StatusReuniao.EM_ANDAMENTO) {
            return "redirect:/coord/reunioes";
        }

        model.addAttribute("reuniao", reuniao);

        // Prepara contagem de votos para cada processo
        Map<Long, Map<String, Long>> votosMap = new HashMap<>();
        if (reuniao.getProcessosEmPauta() != null) {
            for (Processo p : reuniao.getProcessosEmPauta()) {
                votosMap.put(p.getId(), processoService.contarVotos(p.getId()));
            }
        }
        model.addAttribute("votosMap", votosMap);

        // Conta processos julgados
        long julgados = reuniao.getProcessosEmPauta() != null ?
                reuniao.getProcessosEmPauta().stream()
                        .filter(p -> "JULGADO".equals(p.getStatus()))
                        .count() : 0;
        model.addAttribute("totalJulgados", julgados);
        model.addAttribute("totalProcessos", reuniao.getProcessosEmPauta() != null ?
                reuniao.getProcessosEmPauta().size() : 0);

        return "coord/reuniao/conduzir";
    }

    /**
     * Julga um processo (REQFUNC 11)
     */
    @PostMapping("/{reuniaoId}/julgar/{processoId}")
    public String julgarProcesso(@PathVariable Long reuniaoId,
                                  @PathVariable Long processoId,
                                  @RequestParam String resultado,
                                  RedirectAttributes redirect) {
        try {
            Processo processo = processoService.julgarProcesso(processoId, resultado);
            redirect.addFlashAttribute("msg", "Processo julgado: " + processo.getResultado());
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMsg", "Erro ao julgar processo: " + e.getMessage());
        }
        return "redirect:/coord/reunioes/" + reuniaoId + "/conduzir";
    }

    /**
     * Finaliza a sessao (REQFUNC 12)
     */
    @PostMapping("/{id}/finalizar")
    public String finalizarSessao(@PathVariable Long id, RedirectAttributes redirect) {
        Reuniao reuniao = reuniaoService.finalizarSessao(id);
        if (reuniao != null && reuniao.getStatus() == StatusReuniao.ENCERRADA) {
            redirect.addFlashAttribute("msg", "Sessão finalizada com sucesso!");
        } else {
            redirect.addFlashAttribute("errorMsg", "Não foi possível finalizar a sessão.");
        }
        return "redirect:/coord/reunioes";
    }
}

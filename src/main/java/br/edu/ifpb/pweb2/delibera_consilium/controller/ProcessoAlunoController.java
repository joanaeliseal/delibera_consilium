package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;
import br.edu.ifpb.pweb2.delibera_consilium.model.Processo;
import br.edu.ifpb.pweb2.delibera_consilium.security.SecurityUtils;
import br.edu.ifpb.pweb2.delibera_consilium.service.AlunoService;
import br.edu.ifpb.pweb2.delibera_consilium.service.AssuntoService;
import br.edu.ifpb.pweb2.delibera_consilium.service.ProcessoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

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

    /**
     * Busca o aluno logado usando Spring Security
     */
    private Aluno getAlunoLogado() {
        String username = SecurityUtils.getAuthenticatedUsername();
        if (username == null) {
            return null;
        }
        return alunoService.buscarPorLogin(username);
    }

    @GetMapping
    public String listar(@RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "assuntoId", required = false) Long assuntoId,
                         @RequestParam(value = "ordem", required = false) String ordem,
                         Model model,
                         RedirectAttributes redirect) {
        
        // Busca o aluno logado pelo Spring Security
        Aluno alunoLogado = getAlunoLogado();
        
        if (alunoLogado == null) {
            redirect.addFlashAttribute("errorMsg", "Erro: Usuário não autenticado!");
            return "redirect:/login";
        }

        model.addAttribute("processos", processoService.listarPorInteressado(alunoLogado, status, assuntoId, ordem));
        model.addAttribute("alunoLogado", alunoLogado);
        model.addAttribute("assuntos", assuntoService.listarTodos()); 
        
        return "aluno/processo/list";
    }

    @GetMapping("/novo")
    public String novo(Model model, RedirectAttributes redirect) {
        
        // Busca o aluno logado
        Aluno alunoLogado = getAlunoLogado();
        
        if (alunoLogado == null) {
            redirect.addFlashAttribute("errorMsg", "Erro: Usuário não autenticado!");
            return "redirect:/login";
        }
        
        Processo p = new Processo();
        p.setInteressado(alunoLogado); 
        
        model.addAttribute("processo", p);
        model.addAttribute("assuntos", assuntoService.listarTodos());
        model.addAttribute("alunoLogado", alunoLogado);
        
        return "aluno/processo/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Processo processo, 
                         BindingResult result,
                         @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile,
                         Model model, 
                         RedirectAttributes redirect) {
        
        // Busca o aluno logado
        Aluno alunoLogado = getAlunoLogado();
        
        if (alunoLogado == null) {
            redirect.addFlashAttribute("errorMsg", "Erro: Usuário não autenticado!");
            return "redirect:/login";
        }
        
        if (result.hasErrors()) {
            model.addAttribute("assuntos", assuntoService.listarTodos());
            model.addAttribute("alunoLogado", alunoLogado);
            return "aluno/processo/form";
        }

        // IMPORTANTE: Garante que o processo pertença ao aluno logado
        processo.setInteressado(alunoLogado);
        
        // Processa o arquivo PDF se foi enviado
        if (pdfFile != null && !pdfFile.isEmpty()) {
            try {
                // Valida se é realmente um PDF
                String contentType = pdfFile.getContentType();
                if (!"application/pdf".equals(contentType)) {
                    model.addAttribute("errorMsg", "Apenas arquivos PDF são permitidos!");
                    model.addAttribute("assuntos", assuntoService.listarTodos());
                    model.addAttribute("alunoLogado", alunoLogado);
                    return "aluno/processo/form";
                }
                
                // Valida tamanho (máximo 5MB)
                if (pdfFile.getSize() > 5 * 1024 * 1024) {
                    model.addAttribute("errorMsg", "O arquivo PDF não pode ser maior que 5MB!");
                    model.addAttribute("assuntos", assuntoService.listarTodos());
                    model.addAttribute("alunoLogado", alunoLogado);
                    return "aluno/processo/form";
                }
                
                processo.setRequerimentoPdf(pdfFile.getBytes());
                processo.setRequerimentoPdfNome(pdfFile.getOriginalFilename());
            } catch (IOException e) {
                model.addAttribute("errorMsg", "Erro ao processar o arquivo PDF!");
                model.addAttribute("assuntos", assuntoService.listarTodos());
                model.addAttribute("alunoLogado", alunoLogado);
                return "aluno/processo/form";
            }
        }
        
        processoService.salvar(processo);
        
        redirect.addFlashAttribute("msg", "Processo criado com sucesso!");
        return "redirect:/aluno/processos";
    }
    
    @GetMapping("/{id}/upload")
    public String uploadForm(@PathVariable Long id,
                             Model model,
                             RedirectAttributes redirect) {
        
        Aluno alunoLogado = getAlunoLogado();
        
        if (alunoLogado == null) {
            redirect.addFlashAttribute("errorMsg", "Erro: Usuário não autenticado!");
            return "redirect:/login";
        }
        
        Processo processo = processoService.buscarPorId(id);
        
        if (processo == null) {
            redirect.addFlashAttribute("errorMsg", "Processo não encontrado!");
            return "redirect:/aluno/processos";
        }
        
        // Verifica se o processo pertence ao aluno logado
        if (!processo.getInteressado().getId().equals(alunoLogado.getId())) {
            redirect.addFlashAttribute("errorMsg", "Você não tem permissão para acessar este processo!");
            return "redirect:/aluno/processos";
        }
        
        // Verifica se o processo já foi distribuído
        if (!processo.podeReceberUpload()) {
            redirect.addFlashAttribute("errorMsg", "Não é possível fazer upload! O processo já foi distribuído.");
            return "redirect:/aluno/processos";
        }
        
        model.addAttribute("processo", processo);
        model.addAttribute("alunoLogado", alunoLogado);
        
        return "aluno/processo/upload";
    }
    
    @PostMapping("/{id}/upload")
    public String uploadPdf(@PathVariable Long id,
                            @RequestParam("pdfFile") MultipartFile pdfFile,
                            RedirectAttributes redirect) {
        
        Aluno alunoLogado = getAlunoLogado();
        
        if (alunoLogado == null) {
            redirect.addFlashAttribute("errorMsg", "Erro: Usuário não autenticado!");
            return "redirect:/login";
        }
        
        Processo processo = processoService.buscarPorId(id);
        
        if (processo == null) {
            redirect.addFlashAttribute("errorMsg", "Processo não encontrado!");
            return "redirect:/aluno/processos";
        }
        
        // Verifica se o processo pertence ao aluno logado
        if (!processo.getInteressado().getId().equals(alunoLogado.getId())) {
            redirect.addFlashAttribute("errorMsg", "Você não tem permissão para acessar este processo!");
            return "redirect:/aluno/processos";
        }
        
        // Verifica se o processo já foi distribuído
        if (!processo.podeReceberUpload()) {
            redirect.addFlashAttribute("errorMsg", "Não é possível fazer upload! O processo já foi distribuído.");
            return "redirect:/aluno/processos";
        }
        
        // Valida o arquivo
        if (pdfFile.isEmpty()) {
            redirect.addFlashAttribute("errorMsg", "Selecione um arquivo PDF!");
            return "redirect:/aluno/processos/" + id + "/upload";
        }
        
        try {
            // Valida se é realmente um PDF
            String contentType = pdfFile.getContentType();
            if (!"application/pdf".equals(contentType)) {
                redirect.addFlashAttribute("errorMsg", "Apenas arquivos PDF são permitidos!");
                return "redirect:/aluno/processos/" + id + "/upload";
            }
            
            // Valida tamanho (máximo 5MB)
            if (pdfFile.getSize() > 5 * 1024 * 1024) {
                redirect.addFlashAttribute("errorMsg", "O arquivo PDF não pode ser maior que 5MB!");
                return "redirect:/aluno/processos/" + id + "/upload";
            }
            
            // Salva o arquivo
            processo.setRequerimentoPdf(pdfFile.getBytes());
            processo.setRequerimentoPdfNome(pdfFile.getOriginalFilename());
            processoService.salvar(processo);
            
            redirect.addFlashAttribute("msg", "PDF enviado com sucesso!");
            
        } catch (IOException e) {
            redirect.addFlashAttribute("errorMsg", "Erro ao processar o arquivo PDF!");
            return "redirect:/aluno/processos/" + id + "/upload";
        }
        
        return "redirect:/aluno/processos";
    }
    
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id) {
        
        Aluno alunoLogado = getAlunoLogado();
        
        if (alunoLogado == null) {
            return ResponseEntity.status(403).build();
        }
        
        Processo processo = processoService.buscarPorId(id);
        
        if (processo == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Verifica se o processo pertence ao aluno logado
        if (!processo.getInteressado().getId().equals(alunoLogado.getId())) {
            return ResponseEntity.status(403).build();
        }
        
        if (!processo.temRequerimentoPdf()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + processo.getRequerimentoPdfNome() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(processo.getRequerimentoPdf());
    }
}
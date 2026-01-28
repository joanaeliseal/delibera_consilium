package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;
import br.edu.ifpb.pweb2.delibera_consilium.model.Processo;
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

    @GetMapping
    public String listar(@RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "assuntoId", required = false) Long assuntoId,
                         @RequestParam(value = "ordem", required = false) String ordem,
                         @RequestParam(value = "alunoId", required = false, defaultValue = "1") Long alunoId, 
                         Model model) {
        
        Aluno alunoLogado = alunoService.buscarPorId(alunoId);

        if (alunoLogado != null) {
            model.addAttribute("processos", processoService.listarPorInteressado(alunoLogado, status, assuntoId, ordem));
            
            model.addAttribute("alunoId", alunoId);
            model.addAttribute("assuntos", assuntoService.listarTodos()); 
        }
        
        return "aluno/processo/list";
    }

    @GetMapping("/novo")
    public String novo(@RequestParam(value = "alunoId", required = false, defaultValue = "1") Long alunoId, 
                       Model model) {
        
        Processo p = new Processo();
        
        Aluno aluno = alunoService.buscarPorId(alunoId);
        p.setInteressado(aluno); 
        
        model.addAttribute("processo", p);
        model.addAttribute("assuntos", assuntoService.listarTodos());
        model.addAttribute("alunoId", alunoId); 
        
        return "aluno/processo/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Processo processo, 
                         BindingResult result, 
                         @RequestParam("alunoId") Long alunoId,
                         @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile,
                         Model model, 
                         RedirectAttributes redirect) {
        
        if (result.hasErrors()) {
            model.addAttribute("assuntos", assuntoService.listarTodos());
            model.addAttribute("alunoId", alunoId); 
            return "aluno/processo/form";
        }

        // Garante que o processo pertença ao aluno do id informado
        Aluno aluno = alunoService.buscarPorId(alunoId);
        processo.setInteressado(aluno);
        
        // NOVO: Processa o arquivo PDF se foi enviado
        if (pdfFile != null && !pdfFile.isEmpty()) {
            try {
                // Valida se é realmente um PDF
                String contentType = pdfFile.getContentType();
                if (!"application/pdf".equals(contentType)) {
                    model.addAttribute("errorMsg", "Apenas arquivos PDF são permitidos!");
                    model.addAttribute("assuntos", assuntoService.listarTodos());
                    model.addAttribute("alunoId", alunoId);
                    return "aluno/processo/form";
                }
                
                // Valida tamanho (máximo 5MB)
                if (pdfFile.getSize() > 5 * 1024 * 1024) {
                    model.addAttribute("errorMsg", "O arquivo PDF não pode ser maior que 5MB!");
                    model.addAttribute("assuntos", assuntoService.listarTodos());
                    model.addAttribute("alunoId", alunoId);
                    return "aluno/processo/form";
                }
                
                processo.setRequerimentoPdf(pdfFile.getBytes());
                processo.setRequerimentoPdfNome(pdfFile.getOriginalFilename());
            } catch (IOException e) {
                model.addAttribute("errorMsg", "Erro ao processar o arquivo PDF!");
                model.addAttribute("assuntos", assuntoService.listarTodos());
                model.addAttribute("alunoId", alunoId);
                return "aluno/processo/form";
            }
        }
        
        processoService.salvar(processo);
        
        redirect.addFlashAttribute("msg", "Processo criado com sucesso!");
        return "redirect:/aluno/processos?alunoId=" + alunoId;
    }
    
    // NOVO: Rota para fazer upload de PDF em processo existente
    @GetMapping("/{id}/upload")
    public String uploadForm(@PathVariable Long id,
                             @RequestParam("alunoId") Long alunoId,
                             Model model,
                             RedirectAttributes redirect) {
        
        Processo processo = processoService.buscarPorId(id);
        
        if (processo == null) {
            redirect.addFlashAttribute("errorMsg", "Processo não encontrado!");
            return "redirect:/aluno/processos?alunoId=" + alunoId;
        }
        
        // Verifica se o processo pertence ao aluno
        if (!processo.getInteressado().getId().equals(alunoId)) {
            redirect.addFlashAttribute("errorMsg", "Você não tem permissão para acessar este processo!");
            return "redirect:/aluno/processos?alunoId=" + alunoId;
        }
        
        // Verifica se o processo já foi distribuído
        if (!processo.podeReceberUpload()) {
            redirect.addFlashAttribute("errorMsg", "Não é possível fazer upload! O processo já foi distribuído.");
            return "redirect:/aluno/processos?alunoId=" + alunoId;
        }
        
        model.addAttribute("processo", processo);
        model.addAttribute("alunoId", alunoId);
        
        return "aluno/processo/upload";
    }
    
    // NOVO: Processa o upload do PDF
    @PostMapping("/{id}/upload")
    public String uploadPdf(@PathVariable Long id,
                            @RequestParam("alunoId") Long alunoId,
                            @RequestParam("pdfFile") MultipartFile pdfFile,
                            RedirectAttributes redirect) {
        
        Processo processo = processoService.buscarPorId(id);
        
        if (processo == null) {
            redirect.addFlashAttribute("errorMsg", "Processo não encontrado!");
            return "redirect:/aluno/processos?alunoId=" + alunoId;
        }
        
        // Verifica se o processo pertence ao aluno
        if (!processo.getInteressado().getId().equals(alunoId)) {
            redirect.addFlashAttribute("errorMsg", "Você não tem permissão para acessar este processo!");
            return "redirect:/aluno/processos?alunoId=" + alunoId;
        }
        
        // Verifica se o processo já foi distribuído
        if (!processo.podeReceberUpload()) {
            redirect.addFlashAttribute("errorMsg", "Não é possível fazer upload! O processo já foi distribuído.");
            return "redirect:/aluno/processos?alunoId=" + alunoId;
        }
        
        // Valida o arquivo
        if (pdfFile.isEmpty()) {
            redirect.addFlashAttribute("errorMsg", "Selecione um arquivo PDF!");
            return "redirect:/aluno/processos/" + id + "/upload?alunoId=" + alunoId;
        }
        
        try {
            // Valida se é realmente um PDF
            String contentType = pdfFile.getContentType();
            if (!"application/pdf".equals(contentType)) {
                redirect.addFlashAttribute("errorMsg", "Apenas arquivos PDF são permitidos!");
                return "redirect:/aluno/processos/" + id + "/upload?alunoId=" + alunoId;
            }
            
            // Valida tamanho (máximo 5MB)
            if (pdfFile.getSize() > 5 * 1024 * 1024) {
                redirect.addFlashAttribute("errorMsg", "O arquivo PDF não pode ser maior que 5MB!");
                return "redirect:/aluno/processos/" + id + "/upload?alunoId=" + alunoId;
            }
            
            // Salva o arquivo
            processo.setRequerimentoPdf(pdfFile.getBytes());
            processo.setRequerimentoPdfNome(pdfFile.getOriginalFilename());
            processoService.salvar(processo);
            
            redirect.addFlashAttribute("msg", "PDF enviado com sucesso!");
            
        } catch (IOException e) {
            redirect.addFlashAttribute("errorMsg", "Erro ao processar o arquivo PDF!");
            return "redirect:/aluno/processos/" + id + "/upload?alunoId=" + alunoId;
        }
        
        return "redirect:/aluno/processos?alunoId=" + alunoId;
    }
    
    // NOVO: Download do PDF
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id,
                                               @RequestParam("alunoId") Long alunoId) {
        
        Processo processo = processoService.buscarPorId(id);
        
        if (processo == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Verifica se o processo pertence ao aluno
        if (!processo.getInteressado().getId().equals(alunoId)) {
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
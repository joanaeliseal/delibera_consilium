package br.edu.ifpb.pweb2.delibera_consilium.controller;

import br.edu.ifpb.pweb2.delibera_consilium.model.Aluno;
import br.edu.ifpb.pweb2.delibera_consilium.service.AlunoService;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/alunos")
public class AlunoController {

    private final AlunoService service;
    private final PasswordEncoder passwordEncoder;

    public AlunoController(AlunoService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alunos", service.listarTodos());
        return "admin/aluno/list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("aluno", new Aluno());
        return "admin/aluno/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("aluno") Aluno aluno,
                         BindingResult result,
                         RedirectAttributes redirect) {
       
        // Busca no banco se já tem alguém com essa matrícula
        Aluno alunoExistente = service.buscarPorMatricula(aluno.getMatricula());

        // Se encontrou alguém e não sou eu mesmo (para casos de editar)
        if (alunoExistente != null && !alunoExistente.getId().equals(aluno.getId())) {
            result.rejectValue("matricula", "error.matricula", "Esta matrícula já está cadastrada para outro aluno.");
        }

        if (result.hasErrors()) {
            return "admin/aluno/form";
        }

        // ⭐ CRIPTOGRAFA A SENHA AUTOMATICAMENTE
        // Só criptografa se:
        // 1. For um novo aluno (id == null) OU
        // 2. A senha foi alterada (não começa com $2a$ que é o prefixo do BCrypt)
        if (aluno.getId() == null || 
            (aluno.getSenha() != null && !aluno.getSenha().isEmpty() && !aluno.getSenha().startsWith("$2a$"))) {
            String senhaCriptografada = passwordEncoder.encode(aluno.getSenha());
            aluno.setSenha(senhaCriptografada);
        }

        service.salvar(aluno);
        redirect.addFlashAttribute("msg", "Aluno salvo com sucesso!");
        return "redirect:/admin/alunos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Aluno aluno = service.buscarPorId(id);
        // ⭐ IMPORTANTE: Limpa a senha ao editar para não mostrar o hash
        // O admin terá que digitar uma nova senha se quiser alterar
        aluno.setSenha("");
        model.addAttribute("aluno", aluno);
        return "admin/aluno/form";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable("id") Long id, RedirectAttributes redirect) {
        try {
            service.excluir(id);
            redirect.addFlashAttribute("msg", "Aluno excluído com sucesso!");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMsg", "Não é possível excluir este aluno pois existem processos vinculados a ele.");
        }
        return "redirect:/admin/alunos";
    }
}
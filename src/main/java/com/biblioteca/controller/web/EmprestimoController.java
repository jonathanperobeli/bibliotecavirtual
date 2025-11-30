package com.biblioteca.controller.web;

import com.biblioteca.model.dto.EmprestimoDTO;
import com.biblioteca.model.entity.Usuario;
import com.biblioteca.service.EmprestimoService;
import com.biblioteca.service.LivroService;
import com.biblioteca.service.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller Web para gerenciamento de Empréstimos.
 */
@Controller
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;
    private final LivroService livroService;
    private final UsuarioService usuarioService;

    public EmprestimoController(EmprestimoService emprestimoService,
                               LivroService livroService,
                               UsuarioService usuarioService) {
        this.emprestimoService = emprestimoService;
        this.livroService = livroService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String listarTodos(Model model) {
        model.addAttribute("emprestimos", emprestimoService.listarTodos());
        return "emprestimos/lista";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model, @AuthenticationPrincipal Usuario usuario) {
        return emprestimoService.buscarPorId(id)
                .map(emprestimo -> {
                    // Usuário comum só pode ver seus próprios empréstimos
                    if (usuario.getRole() != Usuario.Role.ADMIN && !emprestimo.getUsuarioId().equals(usuario.getId())) {
                        return "redirect:/emprestimos/meus";
                    }
                    model.addAttribute("emprestimo", emprestimo);
                    return "emprestimos/detalhes";
                })
                .orElse("redirect:/emprestimos");
    }

    @GetMapping("/meus")
    public String meusEmprestimos(Model model, @AuthenticationPrincipal Usuario usuario) {
        List<EmprestimoDTO> emprestimos = emprestimoService.listarPorUsuario(usuario.getId());
        model.addAttribute("emprestimos", emprestimos);
        return "emprestimos/meus-emprestimos";
    }

    @GetMapping("/novo")
    public String novoEmprestimo(Model model,
                                @RequestParam(required = false) Long livroId,
                                @AuthenticationPrincipal Usuario usuarioLogado) {
        // Se livroId foi passado, busca o livro
        if (livroId != null) {
            livroService.buscarPorId(livroId).ifPresent(livro -> model.addAttribute("livro", livro));
        }
        
        // Admin pode escolher usuário e ver todos os livros
        if (usuarioLogado.getRole() == Usuario.Role.ADMIN) {
            model.addAttribute("usuarios", usuarioService.listarTodos());
            model.addAttribute("livros", livroService.listarDisponiveis());
        }
        
        model.addAttribute("usuarioLogado", usuarioLogado);
        return "emprestimos/form";
    }

    @PostMapping("/realizar")
    public String realizarEmprestimo(@RequestParam Long usuarioId,
                                    @RequestParam Long livroId,
                                    RedirectAttributes redirectAttributes,
                                    @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            // Usuário comum só pode emprestar para si mesmo
            if (usuarioLogado.getRole() != Usuario.Role.ADMIN) {
                usuarioId = usuarioLogado.getId();
            }
            
            emprestimoService.realizarEmprestimo(usuarioId, livroId);
            redirectAttributes.addFlashAttribute("mensagem", "Empréstimo realizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        
        return usuarioLogado.getRole() == Usuario.Role.ADMIN 
                ? "redirect:/emprestimos" 
                : "redirect:/emprestimos/meus";
    }

    @PostMapping("/emprestar/{livroId}")
    public String emprestarLivro(@PathVariable Long livroId,
                                @AuthenticationPrincipal Usuario usuario,
                                RedirectAttributes redirectAttributes) {
        try {
            emprestimoService.realizarEmprestimo(usuario.getId(), livroId);
            redirectAttributes.addFlashAttribute("mensagem", "Empréstimo realizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/emprestimos/meus";
    }

    @PostMapping("/{id}/devolver")
    public String devolverLivro(@PathVariable Long id,
                               RedirectAttributes redirectAttributes,
                               @AuthenticationPrincipal Usuario usuario) {
        try {
            EmprestimoDTO emprestimo = emprestimoService.realizarDevolucao(id);
            String mensagem = "Livro devolvido com sucesso!";
            if (emprestimo.getMulta() != null && emprestimo.getMulta().doubleValue() > 0) {
                mensagem += String.format(" Multa por atraso: R$ %.2f", emprestimo.getMulta());
            }
            redirectAttributes.addFlashAttribute("mensagem", mensagem);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        
        return usuario.getRole() == Usuario.Role.ADMIN 
                ? "redirect:/emprestimos" 
                : "redirect:/emprestimos/meus";
    }

    @PostMapping("/{id}/renovar")
    public String renovarEmprestimo(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes,
                                   @AuthenticationPrincipal Usuario usuario) {
        try {
            emprestimoService.renovarEmprestimo(id);
            redirectAttributes.addFlashAttribute("mensagem", "Empréstimo renovado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        
        return usuario.getRole() == Usuario.Role.ADMIN 
                ? "redirect:/emprestimos" 
                : "redirect:/emprestimos/meus";
    }

    @PostMapping("/cancelar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String cancelarEmprestimo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            emprestimoService.cancelarEmprestimo(id);
            redirectAttributes.addFlashAttribute("mensagem", "Empréstimo cancelado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/emprestimos";
    }

    @GetMapping("/ativos")
    @PreAuthorize("hasRole('ADMIN')")
    public String listarAtivos(Model model) {
        model.addAttribute("emprestimos", emprestimoService.listarAtivos());
        model.addAttribute("titulo", "Empréstimos Ativos");
        return "emprestimos/lista";
    }

    @GetMapping("/atrasados")
    @PreAuthorize("hasRole('ADMIN')")
    public String listarAtrasados(Model model) {
        model.addAttribute("emprestimos", emprestimoService.listarAtrasados());
        model.addAttribute("titulo", "Empréstimos Atrasados");
        return "emprestimos/lista";
    }
}

package com.biblioteca.controller.web;

import com.biblioteca.model.dto.AutorDTO;
import com.biblioteca.service.AutorService;
import com.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller Web para gerenciamento de Autores.
 */
@Controller
@RequestMapping("/autores")
public class AutorController {

    private final AutorService autorService;
    private final LivroService livroService;

    public AutorController(AutorService autorService, LivroService livroService) {
        this.autorService = autorService;
        this.livroService = livroService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("autores", autorService.listarTodos());
        return "autores/lista";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        return autorService.buscarPorId(id)
                .map(autor -> {
                    model.addAttribute("autor", autor);
                    model.addAttribute("livros", livroService.buscarPorAutor(id));
                    return "autores/detalhes";
                })
                .orElse("redirect:/autores");
    }

    @GetMapping("/novo")
    @PreAuthorize("hasRole('ADMIN')")
    public String novo(Model model) {
        model.addAttribute("autor", new AutorDTO());
        return "autores/form";
    }

    @PostMapping("/salvar")
    @PreAuthorize("hasRole('ADMIN')")
    public String salvar(@Valid @ModelAttribute("autor") AutorDTO autorDTO,
                        BindingResult result,
                        RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "autores/form";
        }

        if (autorDTO.getId() == null) {
            autorService.salvar(autorDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Autor cadastrado com sucesso!");
        } else {
            autorService.atualizar(autorDTO.getId(), autorDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Autor atualizado com sucesso!");
        }

        return "redirect:/autores";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editar(@PathVariable Long id, Model model) {
        return autorService.buscarPorId(id)
                .map(autor -> {
                    model.addAttribute("autor", autor);
                    return "autores/form";
                })
                .orElse("redirect:/autores");
    }

    @PostMapping("/excluir/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            autorService.deletar(id);
            redirectAttributes.addFlashAttribute("mensagem", "Autor excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/autores";
    }
}

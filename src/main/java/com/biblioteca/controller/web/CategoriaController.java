package com.biblioteca.controller.web;

import com.biblioteca.model.dto.CategoriaDTO;
import com.biblioteca.service.CategoriaService;
import com.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller Web para gerenciamento de Categorias.
 */
@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final LivroService livroService;

    public CategoriaController(CategoriaService categoriaService, LivroService livroService) {
        this.categoriaService = categoriaService;
        this.livroService = livroService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "categorias/lista";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        return categoriaService.buscarPorId(id)
                .map(categoria -> {
                    model.addAttribute("categoria", categoria);
                    model.addAttribute("livros", livroService.buscarPorCategoria(id));
                    return "categorias/detalhes";
                })
                .orElse("redirect:/categorias");
    }

    @GetMapping("/nova")
    @PreAuthorize("hasRole('ADMIN')")
    public String nova(Model model) {
        model.addAttribute("categoria", new CategoriaDTO());
        return "categorias/form";
    }

    @PostMapping("/salvar")
    @PreAuthorize("hasRole('ADMIN')")
    public String salvar(@Valid @ModelAttribute("categoria") CategoriaDTO categoriaDTO,
                        BindingResult result,
                        RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "categorias/form";
        }

        if (categoriaDTO.getId() == null) {
            categoriaService.salvar(categoriaDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Categoria cadastrada com sucesso!");
        } else {
            categoriaService.atualizar(categoriaDTO.getId(), categoriaDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Categoria atualizada com sucesso!");
        }

        return "redirect:/categorias";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editar(@PathVariable Long id, Model model) {
        return categoriaService.buscarPorId(id)
                .map(categoria -> {
                    model.addAttribute("categoria", categoria);
                    return "categorias/form";
                })
                .orElse("redirect:/categorias");
    }

    @PostMapping("/excluir/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoriaService.deletar(id);
            redirectAttributes.addFlashAttribute("mensagem", "Categoria excluída com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/categorias";
    }
}

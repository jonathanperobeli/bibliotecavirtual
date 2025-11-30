package com.biblioteca.controller.web;

import com.biblioteca.model.dto.LivroDTO;
import com.biblioteca.service.AutorService;
import com.biblioteca.service.CategoriaService;
import com.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller Web para gerenciamento de Livros.
 */
@Controller
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;
    private final CategoriaService categoriaService;
    private final AutorService autorService;

    public LivroController(LivroService livroService, 
                          CategoriaService categoriaService,
                          AutorService autorService) {
        this.livroService = livroService;
        this.categoriaService = categoriaService;
        this.autorService = autorService;
    }

    @GetMapping
    public String listar(Model model,
                        @RequestParam(required = false) String busca,
                        @PageableDefault(size = 10) Pageable pageable) {
        Page<LivroDTO> livros;
        
        if (busca != null && !busca.isEmpty()) {
            livros = livroService.buscarPorTermo(busca, pageable);
            model.addAttribute("busca", busca);
        } else {
            livros = livroService.listarPaginado(pageable);
        }
        
        model.addAttribute("livros", livros);
        return "livros/lista";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        return livroService.buscarPorId(id)
                .map(livro -> {
                    model.addAttribute("livro", livro);
                    return "livros/detalhes";
                })
                .orElse("redirect:/livros");
    }

    @GetMapping("/novo")
    @PreAuthorize("hasRole('ADMIN')")
    public String novo(Model model) {
        model.addAttribute("livro", new LivroDTO());
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("autores", autorService.listarTodos());
        return "livros/form";
    }

    @PostMapping("/salvar")
    @PreAuthorize("hasRole('ADMIN')")
    public String salvar(@Valid @ModelAttribute("livro") LivroDTO livroDTO,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarTodas());
            model.addAttribute("autores", autorService.listarTodos());
            return "livros/form";
        }

        if (livroDTO.getId() == null) {
            livroService.salvar(livroDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Livro cadastrado com sucesso!");
        } else {
            livroService.atualizar(livroDTO.getId(), livroDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Livro atualizado com sucesso!");
        }

        return "redirect:/livros";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editar(@PathVariable Long id, Model model) {
        return livroService.buscarPorId(id)
                .map(livro -> {
                    model.addAttribute("livro", livro);
                    model.addAttribute("categorias", categoriaService.listarTodas());
                    model.addAttribute("autores", autorService.listarTodos());
                    return "livros/form";
                })
                .orElse("redirect:/livros");
    }

    @PostMapping("/excluir/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            livroService.deletar(id);
            redirectAttributes.addFlashAttribute("mensagem", "Livro excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/livros";
    }

    @GetMapping("/categoria/{id}")
    public String listarPorCategoria(@PathVariable Long id, Model model) {
        model.addAttribute("livros", livroService.buscarPorCategoria(id));
        categoriaService.buscarPorId(id).ifPresent(cat -> 
            model.addAttribute("categoria", cat));
        return "livros/lista-categoria";
    }

    @GetMapping("/disponiveis")
    public String listarDisponiveis(Model model) {
        model.addAttribute("livros", livroService.listarDisponiveis());
        return "livros/disponiveis";
    }
}

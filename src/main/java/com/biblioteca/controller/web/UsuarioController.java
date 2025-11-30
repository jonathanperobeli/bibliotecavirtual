package com.biblioteca.controller.web;

import com.biblioteca.model.dto.UsuarioDTO;
import com.biblioteca.model.entity.Usuario;
import com.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller Web para gerenciamento de Usuários (Admin).
 */
@Controller
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/usuarios/lista";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        return usuarioService.buscarPorId(id)
                .map(usuario -> {
                    model.addAttribute("usuario", usuario);
                    return "admin/usuarios/detalhes";
                })
                .orElse("redirect:/admin/usuarios");
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        model.addAttribute("roles", Usuario.Role.values());
        return "admin/usuarios/form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                        BindingResult result,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("roles", Usuario.Role.values());
            return "admin/usuarios/form";
        }

        if (usuarioDTO.getId() == null) {
            usuarioService.salvar(usuarioDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");
        } else {
            usuarioService.atualizar(usuarioDTO.getId(), usuarioDTO);
            redirectAttributes.addFlashAttribute("mensagem", "Usuário atualizado com sucesso!");
        }

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        return usuarioService.buscarPorId(id)
                .map(usuario -> {
                    model.addAttribute("usuario", usuario);
                    model.addAttribute("roles", Usuario.Role.values());
                    return "admin/usuarios/form";
                })
                .orElse("redirect:/admin/usuarios");
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deletar(id);
            redirectAttributes.addFlashAttribute("mensagem", "Usuário excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/ativar/{id}")
    public String ativarDesativar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.buscarPorId(id).ifPresent(usuario -> {
            usuario.setAtivo(!usuario.getAtivo());
            usuarioService.atualizar(id, usuario);
            String acao = usuario.getAtivo() ? "ativado" : "desativado";
            redirectAttributes.addFlashAttribute("mensagem", "Usuário " + acao + " com sucesso!");
        });
        return "redirect:/admin/usuarios";
    }
}

package com.biblioteca.controller.web;

import com.biblioteca.model.dto.DashboardDTO;
import com.biblioteca.model.dto.UsuarioDTO;
import com.biblioteca.model.entity.Usuario;
import com.biblioteca.service.DashboardService;
import com.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller Web para páginas principais e autenticação.
 */
@Controller
public class HomeController {

    private final DashboardService dashboardService;
    private final UsuarioService usuarioService;

    public HomeController(DashboardService dashboardService, UsuarioService usuarioService) {
        this.dashboardService = dashboardService;
        this.usuarioService = usuarioService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        DashboardDTO stats = dashboardService.obterEstatisticas();
        model.addAttribute("stats", stats);
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                          BindingResult result,
                          RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "register";
        }

        if (usuarioService.emailExiste(usuarioDTO.getEmail())) {
            result.rejectValue("email", "error.usuario", "Email já cadastrado");
            return "register";
        }

        usuarioDTO.setRole(Usuario.Role.USER);
        usuarioDTO.setAtivo(true);
        usuarioService.salvar(usuarioDTO);

        redirectAttributes.addFlashAttribute("mensagem", "Cadastro realizado com sucesso! Faça login.");
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal Usuario usuario) {
        DashboardDTO stats = dashboardService.obterEstatisticas();
        model.addAttribute("stats", stats);
        model.addAttribute("usuarioLogado", usuario);
        return "dashboard";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @GetMapping("/perfil")
    public String perfil(Model model, @AuthenticationPrincipal Usuario usuario) {
        UsuarioDTO usuarioDTO = UsuarioDTO.fromEntity(usuario);
        model.addAttribute("usuario", usuarioDTO);
        return "perfil";
    }

    @PostMapping("/perfil")
    public String atualizarPerfil(@Valid @ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                                  BindingResult result,
                                  @AuthenticationPrincipal Usuario usuario,
                                  RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "perfil";
        }

        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setRole(usuario.getRole()); // Mantém o role atual
        usuarioService.atualizar(usuario.getId(), usuarioDTO);

        redirectAttributes.addFlashAttribute("mensagem", "Perfil atualizado com sucesso!");
        return "redirect:/perfil";
    }
}

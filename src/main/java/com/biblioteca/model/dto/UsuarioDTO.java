package com.biblioteca.model.dto;

import com.biblioteca.model.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO para operações de criação e atualização de usuários.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String senha;

    private String telefone;

    private String endereco;

    private Usuario.Role role;

    private Boolean ativo;

    private LocalDateTime dataCadastro;

    /**
     * Converte DTO para entidade.
     */
    public Usuario toEntity() {
        return Usuario.builder()
                .id(this.id)
                .nome(this.nome)
                .email(this.email)
                .senha(this.senha)
                .telefone(this.telefone)
                .endereco(this.endereco)
                .role(this.role != null ? this.role : Usuario.Role.USER)
                .ativo(this.ativo != null ? this.ativo : true)
                .build();
    }

    /**
     * Cria DTO a partir de entidade.
     */
    public static UsuarioDTO fromEntity(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .telefone(usuario.getTelefone())
                .endereco(usuario.getEndereco())
                .role(usuario.getRole())
                .ativo(usuario.isAtivo())
                .dataCadastro(usuario.getDataCadastro())
                .build();
    }
}

package com.biblioteca.config;

import com.biblioteca.model.entity.*;
import com.biblioteca.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Configuração para inicialização de dados de demonstração.
 * Executa apenas no perfil de desenvolvimento.
 */
@Configuration
@Profile("dev")
public class DataInitConfig {

    @Bean
    CommandLineRunner initDatabase(
            UsuarioRepository usuarioRepository,
            AutorRepository autorRepository,
            CategoriaRepository categoriaRepository,
            LivroRepository livroRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            // Verificar se já existem dados
            if (usuarioRepository.count() > 0) {
                return;
            }

            System.out.println("=== Inicializando dados de demonstração ===");

            // Criar usuários
            Usuario admin = Usuario.builder()
                    .nome("Administrador")
                    .email("admin@biblioteca.com")
                    .senha(passwordEncoder.encode("admin123"))
                    .role(Usuario.Role.ADMIN)
                    .ativo(true)
                    .telefone("(11) 99999-9999")
                    .endereco("Rua da Biblioteca, 100")
                    .build();

            Usuario usuario1 = Usuario.builder()
                    .nome("João Silva")
                    .email("joao@email.com")
                    .senha(passwordEncoder.encode("123456"))
                    .role(Usuario.Role.USER)
                    .ativo(true)
                    .telefone("(11) 98888-8888")
                    .endereco("Av. das Flores, 200")
                    .build();

            Usuario usuario2 = Usuario.builder()
                    .nome("Maria Santos")
                    .email("maria@email.com")
                    .senha(passwordEncoder.encode("123456"))
                    .role(Usuario.Role.USER)
                    .ativo(true)
                    .telefone("(11) 97777-7777")
                    .endereco("Rua das Árvores, 300")
                    .build();

            usuarioRepository.saveAll(Arrays.asList(admin, usuario1, usuario2));

            // Criar categorias
            Categoria ficcao = Categoria.builder()
                    .nome("Ficção")
                    .descricao("Livros de ficção e romance")
                    .build();

            Categoria tecnologia = Categoria.builder()
                    .nome("Tecnologia")
                    .descricao("Livros sobre programação e tecnologia")
                    .build();

            Categoria ciencias = Categoria.builder()
                    .nome("Ciências")
                    .descricao("Livros de ciências naturais e exatas")
                    .build();

            Categoria historia = Categoria.builder()
                    .nome("História")
                    .descricao("Livros sobre história mundial e brasileira")
                    .build();

            Categoria autoajuda = Categoria.builder()
                    .nome("Autoajuda")
                    .descricao("Livros de desenvolvimento pessoal")
                    .build();

            categoriaRepository.saveAll(Arrays.asList(ficcao, tecnologia, ciencias, historia, autoajuda));

            // Criar autores
            Autor machado = Autor.builder()
                    .nome("Machado de Assis")
                    .nacionalidade("Brasileira")
                    .dataNascimento(LocalDate.of(1839, 6, 21))
                    .biografia("Joaquim Maria Machado de Assis foi um escritor brasileiro, considerado o maior nome da literatura brasileira.")
                    .build();

            Autor clarice = Autor.builder()
                    .nome("Clarice Lispector")
                    .nacionalidade("Brasileira")
                    .dataNascimento(LocalDate.of(1920, 12, 10))
                    .biografia("Clarice Lispector foi uma escritora e jornalista nascida na Ucrânia e naturalizada brasileira.")
                    .build();

            Autor martinFowler = Autor.builder()
                    .nome("Martin Fowler")
                    .nacionalidade("Britânica")
                    .biografia("Martin Fowler é um engenheiro de software britânico, autor e palestrante internacional.")
                    .build();

            Autor robertMartin = Autor.builder()
                    .nome("Robert C. Martin")
                    .nacionalidade("Americana")
                    .biografia("Robert Cecil Martin, conhecido como Uncle Bob, é um engenheiro de software e autor americano.")
                    .build();

            Autor carlSagan = Autor.builder()
                    .nome("Carl Sagan")
                    .nacionalidade("Americana")
                    .dataNascimento(LocalDate.of(1934, 11, 9))
                    .biografia("Carl Edward Sagan foi um cientista planetário, astrônomo e divulgador científico americano.")
                    .build();

            autorRepository.saveAll(Arrays.asList(machado, clarice, martinFowler, robertMartin, carlSagan));

            // Criar livros
            Set<Autor> autoresDomCasmurro = new HashSet<>();
            autoresDomCasmurro.add(machado);

            Livro domCasmurro = Livro.builder()
                    .titulo("Dom Casmurro")
                    .isbn("978-85-359-0277-1")
                    .anoPublicacao(1899)
                    .editora("Companhia das Letras")
                    .edicao("1ª Edição")
                    .numeroPaginas(256)
                    .sinopse("Dom Casmurro é um romance escrito por Machado de Assis. É considerada uma das obras mais importantes da literatura brasileira.")
                    .quantidadeTotal(5)
                    .quantidadeDisponivel(5)
                    .status(Livro.StatusLivro.DISPONIVEL)
                    .categoria(ficcao)
                    .autores(autoresDomCasmurro)
                    .build();

            Set<Autor> autoresHoraEstrela = new HashSet<>();
            autoresHoraEstrela.add(clarice);

            Livro horaEstrela = Livro.builder()
                    .titulo("A Hora da Estrela")
                    .isbn("978-85-325-0048-1")
                    .anoPublicacao(1977)
                    .editora("Rocco")
                    .edicao("23ª Edição")
                    .numeroPaginas(96)
                    .sinopse("A Hora da Estrela é o último romance de Clarice Lispector, publicado pouco antes de sua morte.")
                    .quantidadeTotal(3)
                    .quantidadeDisponivel(3)
                    .status(Livro.StatusLivro.DISPONIVEL)
                    .categoria(ficcao)
                    .autores(autoresHoraEstrela)
                    .build();

            Set<Autor> autoresCleanCode = new HashSet<>();
            autoresCleanCode.add(robertMartin);

            Livro cleanCode = Livro.builder()
                    .titulo("Clean Code: A Handbook of Agile Software Craftsmanship")
                    .isbn("978-0-13-235088-4")
                    .anoPublicacao(2008)
                    .editora("Prentice Hall")
                    .edicao("1ª Edição")
                    .numeroPaginas(464)
                    .sinopse("Mesmo código ruim pode funcionar. Mas se o código não for limpo, ele pode colocar uma organização de joelhos.")
                    .quantidadeTotal(4)
                    .quantidadeDisponivel(4)
                    .status(Livro.StatusLivro.DISPONIVEL)
                    .categoria(tecnologia)
                    .autores(autoresCleanCode)
                    .build();

            Set<Autor> autoresRefactoring = new HashSet<>();
            autoresRefactoring.add(martinFowler);

            Livro refactoring = Livro.builder()
                    .titulo("Refactoring: Improving the Design of Existing Code")
                    .isbn("978-0-13-475759-9")
                    .anoPublicacao(2018)
                    .editora("Addison-Wesley")
                    .edicao("2ª Edição")
                    .numeroPaginas(448)
                    .sinopse("A refatoração é uma técnica controlada para melhorar o design de uma base de código existente.")
                    .quantidadeTotal(2)
                    .quantidadeDisponivel(2)
                    .status(Livro.StatusLivro.DISPONIVEL)
                    .categoria(tecnologia)
                    .autores(autoresRefactoring)
                    .build();

            Set<Autor> autoresCosmos = new HashSet<>();
            autoresCosmos.add(carlSagan);

            Livro cosmos = Livro.builder()
                    .titulo("Cosmos")
                    .isbn("978-85-359-0894-0")
                    .anoPublicacao(1980)
                    .editora("Companhia das Letras")
                    .edicao("1ª Edição")
                    .numeroPaginas(496)
                    .sinopse("Cosmos é um livro de divulgação científica escrito por Carl Sagan, publicado em 1980.")
                    .quantidadeTotal(3)
                    .quantidadeDisponivel(3)
                    .status(Livro.StatusLivro.DISPONIVEL)
                    .categoria(ciencias)
                    .autores(autoresCosmos)
                    .build();

            livroRepository.saveAll(Arrays.asList(domCasmurro, horaEstrela, cleanCode, refactoring, cosmos));

            System.out.println("=== Dados de demonstração inicializados com sucesso! ===");
            System.out.println("Usuário admin: admin@biblioteca.com / admin123");
            System.out.println("Usuário comum: joao@email.com / 123456");
        };
    }
}

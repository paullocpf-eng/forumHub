package forumhub.api.controller;

import forumhub.api.domain.topico.DadosAtualizacaoTopico;
import forumhub.api.domain.topico.DadosCadastroTopico;
import forumhub.api.domain.topico.DadosDetalhamentoTopico;
import forumhub.api.domain.topico.Topico;
import forumhub.api.domain.topico.TopicoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroTopico dados, UriComponentsBuilder uriBuilder) {
        // REGRA DE NEGÓCIO: Verificar duplicidade
        var existeDuplicado = repository.findByTituloAndMensagem(dados.titulo(), dados.mensagem());
        if (existeDuplicado.isPresent()) {
            return ResponseEntity.badRequest().body("Já existe um tópico com o mesmo título e mensagem.");
        }

        var topico = new Topico(dados);
        repository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoTopico(topico));
    }

    @GetMapping
    public ResponseEntity<List<DadosDetalhamentoTopico>> listar() {
        var lista = repository.findAll().stream()
                .map(DadosDetalhamentoTopico::new)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var topicoOptional = repository.findById(id);

        if (topicoOptional.isPresent()) {
            return ResponseEntity.ok(new DadosDetalhamentoTopico(topicoOptional.get()));
        }
        // Se o ID não for encontrado retorna erro HTTP 404 Not Found
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoTopico dados) {
        // Verifica se o ID existe
        var topicoOptional = repository.findById(id);
        if (!topicoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        var topico = topicoOptional.get();

        // Verifica se ja existe outro topico igual
        if (dados.titulo() != null && dados.mensagem() != null) {
            var existeDuplicado = repository.findByTituloAndMensagem(dados.titulo(), dados.mensagem());
            if (existeDuplicado.isPresent() && !existeDuplicado.get().getId().equals(id)) {
                return ResponseEntity.badRequest().body("Já existe outro tópico com o mesmo título e mensagem.");
            }
        }
        topico.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        // Busca o tópico no banco para verificar se ele existe
        var topicoOptional = repository.findById(id);

        // Verifica se está presente (conforme as regras)
        if (topicoOptional.isPresent()) {
            repository.deleteById(id); // Deleta do banco de dados
            return ResponseEntity.noContent().build(); // Retorna 204 (Sucesso, sem conteúdo)
        }

        // Se não existir, retorna 404 Not Found
        return ResponseEntity.notFound().build();
    }
}

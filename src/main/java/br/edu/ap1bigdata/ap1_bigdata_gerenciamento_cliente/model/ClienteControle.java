package br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteControle {

    @Autowired
    private ClienteServico clienteServico;

    @GetMapping
    public List<Cliente> listarTodos() {
        return clienteServico.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable int id) {
        return clienteServico.buscarPorId(id)
            .map(cliente -> ResponseEntity.ok(cliente))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Cliente salvar(@RequestBody Cliente cliente) {
        return clienteServico.salvar(cliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable int id) {
        clienteServico.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}

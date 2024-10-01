package br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Controle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Repositorio.ClienteRepositorio;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Repositorio.EnderecoRepositorio;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model.Cliente;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model.Endereco;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes/{id}/enderecos")
public class EnderecoControle {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private EnderecoRepositorio enderecoRepositorio;

    @GetMapping
    public ResponseEntity<List<Endereco>> listarTodos(@PathVariable("id") int idCliente) {
    return this.clienteRepositorio.findById(idCliente)
                                  .map(cliente -> new ResponseEntity<>(cliente.getEnderecos(), HttpStatus.OK))
                                  .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
}

    @GetMapping("/{idEndereco}")
    public ResponseEntity<Endereco> buscarPorId(@PathVariable("idEndereco") int idEndereco) {
        return this.enderecoRepositorio.findById(idEndereco)
                                    .map(endereco -> new ResponseEntity<>(endereco, HttpStatus.OK))
                                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Endereco> salvar(@PathVariable("id") int idCliente, @Valid @RequestBody Endereco endereco) {
        Optional<Cliente> optCliente = this.clienteRepositorio.findById(idCliente);

        if (optCliente.isPresent() == false) 
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        
        Cliente cliente = optCliente.get();
        cliente.getEnderecos().add(endereco);

        this.enderecoRepositorio.save(endereco);
        this.clienteRepositorio.save(cliente);

        return new ResponseEntity<>(endereco, HttpStatus.CREATED);
    }

    @DeleteMapping("{idEndereco}")
    public ResponseEntity<Void> delete(@PathVariable("id") int idCliente, @PathVariable("idEndereco") int idEndereco) {
        Optional<Cliente> optCliente = this.clienteRepositorio.findById(idCliente);
        Optional<Endereco> optEndereco = this.enderecoRepositorio.findById(idEndereco);

        if (optCliente.isPresent() == false) 
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (optEndereco.isPresent() == false) 
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        this.enderecoRepositorio.delete(optEndereco.get());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

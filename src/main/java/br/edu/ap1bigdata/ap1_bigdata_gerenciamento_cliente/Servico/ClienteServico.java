package br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Servico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Repositorio.ClienteRepositorio;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model.Cliente;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model.Endereco;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServico {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    public List<Cliente> listarTodos() {
        return clienteRepositorio.findAll();
    }

    public Optional<Cliente> buscarPorId(int id) {
        return clienteRepositorio.findById(id);
    }

    public Cliente salvar(Cliente cliente) throws IllegalArgumentException {

        cliente.setNome(cliente.getNome());
        cliente.setEmail(cliente.getEmail());
        cliente.setCpf(cliente.getCpf());
        cliente.setTelefone(cliente.getTelefone());
        cliente.setDataNascimento(cliente.getDataNascimento());
        
        if (cliente.getIdade() < 18) {
            throw new IllegalArgumentException("O cliente é menor de idade (idade: " + cliente.getIdade() + " anos). Somente maiores de 18 anos podem ser cadastrados.");
        }

        return clienteRepositorio.save(cliente);
    }

    public Cliente atualizarCliente(int id, Cliente clienteAtualizado) {
        Optional<Cliente> clienteExistente = clienteRepositorio.findById(id);
        if (!clienteExistente.isPresent()) {
            throw new IllegalArgumentException("Cliente com ID " + id + " não encontrado.");
        }

        Cliente cliente = clienteExistente.get();
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setEmail(clienteAtualizado.getEmail());
        cliente.setCpf(clienteAtualizado.getCpf());
        cliente.setTelefone(clienteAtualizado.getTelefone());
        cliente.setDataNascimento(clienteAtualizado.getDataNascimento());
        
        return clienteRepositorio.save(cliente);
    }

    public Cliente adicionarEndereco(int clienteId, Endereco novoEndereco) {
        Optional<Cliente> clienteExistente = clienteRepositorio.findById(clienteId);
        if (!clienteExistente.isPresent()) {
            throw new IllegalArgumentException("Cliente com ID " + clienteId + " não encontrado.");
        }
    
        Cliente cliente = clienteExistente.get();
        Cliente.getEnderecos().add(novoEndereco);
        return clienteRepositorio.save(cliente);
    }

    public Cliente atualizarEndereco(int clienteId, int enderecoId, Endereco enderecoAtualizado) {
        Optional<Cliente> clienteExistente = clienteRepositorio.findById(clienteId);
        if (!clienteExistente.isPresent()) {
            throw new IllegalArgumentException("Cliente com ID " + clienteId + " não encontrado.");
        }
    
        Cliente cliente = clienteExistente.get();
        Endereco endereco = Cliente.getEnderecos().stream()
            .filter(e -> e.getId() == (enderecoId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Endereço com ID " + enderecoId + " não encontrado."));
    
        endereco.setRua(enderecoAtualizado.getRua());
        endereco.setNumero(enderecoAtualizado.getNumero());
        endereco.setBairro(enderecoAtualizado.getBairro());
        endereco.setCidade(enderecoAtualizado.getCidade());
        endereco.setEstado(enderecoAtualizado.getEstado());
        endereco.setCep(enderecoAtualizado.getCep());
    
        return clienteRepositorio.save(cliente);
    }

    public Cliente removerEndereco(int clienteId, int enderecoId) {
        Optional<Cliente> clienteExistente = clienteRepositorio.findById(clienteId);
        if (!clienteExistente.isPresent()) {
            throw new IllegalArgumentException("Cliente com ID " + clienteId + " não encontrado.");
        }
    
        Cliente cliente = clienteExistente.get();
        Endereco endereco = Cliente.getEnderecos().stream()
            .filter(e -> e.getId() == (enderecoId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Endereço com ID " + enderecoId + " não encontrado."));
    
        Cliente.getEnderecos().remove(endereco);
        return clienteRepositorio.save(cliente);
    }
    
    public Cliente buscarClienteComEnderecos(int clienteId) {
        Optional<Cliente> clienteExistente = clienteRepositorio.findById(clienteId);
        if (!clienteExistente.isPresent()) {
            throw new IllegalArgumentException("Cliente com ID " + clienteId + " não encontrado.");
        }
    
        return clienteExistente.get();
    }
    
    public void deletarPorId(int id) {
        clienteRepositorio.deleteById(id);
    }
}

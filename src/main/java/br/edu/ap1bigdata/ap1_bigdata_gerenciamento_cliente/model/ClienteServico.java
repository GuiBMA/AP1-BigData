package br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        Optional<Cliente> clienteExistentePorEmail = clienteRepositorio.findByEmail(cliente.getEmail());
        if (clienteExistentePorEmail.isPresent()) {
            throw new IllegalArgumentException("O email '" + cliente.getEmail() + "' já está em uso. Por favor, utilize um email diferente.");
        }

        Optional<Cliente> clienteExistentePorCpf = clienteRepositorio.findByCpf(cliente.getCpf());
        if (clienteExistentePorCpf.isPresent()) {
            throw new IllegalArgumentException("O CPF '" + cliente.getCpf() + "' já está cadastrado. Verifique o CPF e tente novamente.");
        }

        Optional<Cliente> clienteExistentePorTelefone = clienteRepositorio.findByTelefone(cliente.getTelefone());
        if (clienteExistentePorTelefone.isPresent()) {
            throw new IllegalArgumentException("O telefone '" + cliente.getTelefone() + "' já está em uso. Informe um telefone diferente.");
        }

        if (cliente.getIdade() < 18) {
            throw new IllegalArgumentException("O cliente é menor de idade (idade: " + cliente.getIdade() + " anos). Somente maiores de 18 anos podem ser cadastrados.");
        }

        return clienteRepositorio.save(cliente);
    }

    public void deletarPorId(int id) {
        clienteRepositorio.deleteById(id);
    }
}

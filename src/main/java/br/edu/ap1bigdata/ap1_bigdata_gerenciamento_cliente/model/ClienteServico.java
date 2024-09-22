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
        if (cliente.getIdade() < 18) {
            throw new IllegalArgumentException("O cliente é menor de idade e não pode ser cadastrado.");
        }
        return clienteRepositorio.save(cliente);
    }

    public void deletarPorId(int id) {
        clienteRepositorio.deleteById(id);
    }
}

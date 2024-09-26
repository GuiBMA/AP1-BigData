package br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Servico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Repositorio.EnderecoRepositorio;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model.Endereco;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoServico {

    @Autowired
    private EnderecoRepositorio enderecoRepository;

    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }

    public Optional<Endereco> buscarPorId(int id) {
        return enderecoRepository.findById(id);
    }

    public Endereco salvar(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    public void deletarPorId(int id) {
        enderecoRepository.deleteById(id);
    }
}

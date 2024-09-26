package br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Repositorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model.Endereco;

@Repository
public interface EnderecoRepositorio extends JpaRepository<Endereco, Integer> {
}

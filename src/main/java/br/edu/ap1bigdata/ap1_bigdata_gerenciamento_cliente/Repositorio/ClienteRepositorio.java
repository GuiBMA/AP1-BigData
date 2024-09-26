package br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Repositorio;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model.Cliente;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByEmail(String email);

    Optional<Cliente> findByCpf(String cpf);

    Optional<Cliente> findByTelefone(String telefone);
}

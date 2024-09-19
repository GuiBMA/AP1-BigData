package br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model.Cliente;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model.Endereco;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Ap1GerenciamentoCliTests {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testClienteEhAdulto() {
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao.silva@example.com");
        cliente.setCpf("123.456.789-00");
        cliente.setDataNascimento(LocalDate.of(2000, 1, 1));

        assertTrue(cliente.ehAdulto(), "O cliente deve ser considerado adulto.");
    }

    @Test
    void testClienteMenorDeIdade() {
        Cliente cliente = new Cliente();
        cliente.setNome("Ana Silva");
        cliente.setEmail("ana.silva@example.com");
        cliente.setCpf("123.456.789-00");
        cliente.setDataNascimento(LocalDate.of(2010, 1, 1));

        assertFalse(cliente.ehAdulto(), "O cliente não deve ser considerado adulto.");
    }

    @Test
    void testEmailValido() {
        Cliente cliente = new Cliente();
        cliente.setNome("Maria Silva");
        cliente.setEmail("maria.silva@example.com");
        cliente.setCpf("123.456.789-00");
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertTrue(violations.isEmpty(), "Não deve haver violações se o e-mail for válido.");
    }

    @Test
    void testCpfFormatoInvalido() {
        Cliente cliente = new Cliente();
        cliente.setNome("Carlos Souza");
        cliente.setEmail("carlos.souza@example.com");
        cliente.setCpf("12345678900");
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));

        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertFalse(violations.isEmpty(), "O CPF deve seguir o formato XXX.XXX.XXX-XX.");
    }

    @Test
    void testTelefoneFormatoInvalido() {
        Cliente cliente = new Cliente();
        cliente.setNome("Fernanda Souza");
        cliente.setEmail("fernanda.souza@example.com");
        cliente.setCpf("123.456.789-00");
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));
        cliente.setTelefone("999999999");

        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertFalse(violations.isEmpty(), "O telefone deve seguir o formato (XX) XXXXX-XXXX.");
    }

    @Test
    void testEnderecoCepValido() {
        Endereco endereco = new Endereco();
        endereco.setRua("Rua A");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("São Paulo");
        endereco.setEstado("SP");
        endereco.setCep("12345-678");

        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);
        assertTrue(violations.isEmpty(), "O CEP deve ser válido no formato XXXXX-XXX.");
    }

    @Test
    void testEnderecoCepInvalido() {
        Endereco endereco = new Endereco();
        endereco.setRua("Rua B");
        endereco.setNumero("456");
        endereco.setBairro("Vila");
        endereco.setCidade("Rio de Janeiro");
        endereco.setEstado("RJ");
        endereco.setCep("12345678");

        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);
        assertFalse(violations.isEmpty(), "O CEP deve seguir o formato XXXXX-XXX.");
    }
}

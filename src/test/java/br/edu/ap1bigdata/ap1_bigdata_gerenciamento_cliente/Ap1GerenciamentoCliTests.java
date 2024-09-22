package br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model.Cliente;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model.ClienteRepositorio;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model.ClienteServico;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model.Endereco;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class Ap1GerenciamentoCliTests {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Mock
    private ClienteRepositorio clienteRepositorio;

    @InjectMocks
    private ClienteServico clienteServico;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveLancarExcecaoQuandoCpfDuplicado() {
        Cliente cliente = new Cliente();
        cliente.setNome("Carlos Almeida");
        cliente.setCpf("111.222.333-44");
        cliente.setEmail("carlos.almeida@example.com");
        cliente.setTelefone("(21) 99999-1234");
        cliente.setDataNascimento(LocalDate.of(1995, 8, 22));

        when(clienteRepositorio.findByCpf(cliente.getCpf())).thenReturn(Optional.of(cliente));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteServico.salvar(cliente);
        });

        assertEquals("O CPF '111.222.333-44' já está cadastrado. Verifique o CPF e tente novamente.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoEmailDuplicado() {
        Cliente cliente = new Cliente();
        cliente.setNome("Carlos Almeida");
        cliente.setCpf("111.222.333-44");
        cliente.setEmail("carlos.almeida@example.com");
        cliente.setTelefone("(21) 99999-1234");
        cliente.setDataNascimento(LocalDate.of(1995, 8, 22));

        when(clienteRepositorio.findByEmail(cliente.getEmail())).thenReturn(Optional.of(cliente));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteServico.salvar(cliente);
        });

        assertEquals("O email 'carlos.almeida@example.com' já está em uso. Por favor, utilize um email diferente.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoTelefoneDuplicado() {
        Cliente cliente = new Cliente();
        cliente.setNome("Carlos Almeida");
        cliente.setCpf("111.222.333-44");
        cliente.setEmail("carlos.almeida@example.com");
        cliente.setTelefone("(21) 99999-1234");
        cliente.setDataNascimento(LocalDate.of(1995, 8, 22));
    
        when(clienteRepositorio.findByTelefone(cliente.getTelefone())).thenReturn(Optional.of(cliente));
    
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteServico.salvar(cliente);
        });
    
        assertEquals("O telefone '(21) 99999-1234' já está em uso. Informe um telefone diferente.", exception.getMessage());
    }
    
    @Test
    void deveLancarExcecaoQuandoClienteMenorDeIdade() {
        Cliente cliente = new Cliente();
        cliente.setNome("João Pereira");
        cliente.setCpf("111.222.333-44");
        cliente.setEmail("joao.pereira@example.com");
        cliente.setTelefone("(21) 91234-5678");
        cliente.setDataNascimento(LocalDate.of(2010, 9, 20));
    
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteServico.salvar(cliente);
        });
    
        assertEquals("O cliente é menor de idade (idade: 14 anos). Somente maiores de 18 anos podem ser cadastrados.", exception.getMessage());
    }
    
    @Test
    void deveCadastrarClienteComSucesso() {
        Cliente cliente = new Cliente();
        cliente.setNome("Paulo Silva");
        cliente.setCpf("111.222.333-44");
        cliente.setEmail("paulo.silva@example.com");
        cliente.setTelefone("(21) 91234-5678");
        cliente.setDataNascimento(LocalDate.of(1990, 5, 15));
    
        when(clienteRepositorio.findByCpf(cliente.getCpf())).thenReturn(Optional.empty());
        when(clienteRepositorio.findByEmail(cliente.getEmail())).thenReturn(Optional.empty());
        when(clienteRepositorio.findByTelefone(cliente.getTelefone())).thenReturn(Optional.empty());
        when(clienteRepositorio.save(cliente)).thenReturn(cliente);
    
        Cliente clienteSalvo = clienteServico.salvar(cliente);
    
        assertNotNull(clienteSalvo);
        assertEquals("Paulo Silva", clienteSalvo.getNome());
        assertEquals("111.222.333-44", clienteSalvo.getCpf());
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

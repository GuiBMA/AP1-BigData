package br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Repositorio.ClienteRepositorio;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Servico.ClienteServico;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model.Cliente;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class Ap1GerenciamentoCliTests {

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    private Cliente cliente;

    @Mock
    private ClienteRepositorio clienteRepositorio;

    @InjectMocks
    private ClienteServico clienteServico;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente();
        cliente.setNome("Carlos Almeida");
        cliente.setCpf("111.222.333-44");
        cliente.setEmail("carlos.almeida@example.com");
        cliente.setTelefone("(21) 99999-1234");
        cliente.setDataNascimento(LocalDate.of(1995, 8, 22));
    }

    @Test
    public void deveLancarExcecaoQuandoCpfDuplicado() {
        // Simular que o CPF já existe no banco de dados
        when(clienteRepositorio.findByCpf(cliente.getCpf())).thenReturn(Optional.of(cliente));

        // Tentar salvar o cliente e esperar que a exceção seja lançada
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteServico.salvar(cliente);
        });

        // Verificar se a mensagem da exceção está correta
        assertEquals("O CPF '111.222.333-44' já está cadastrado. Verifique o CPF e tente novamente.", exception.getMessage());

        // Verificar se o método findByCpf foi chamado no repositório
        verify(clienteRepositorio, times(1)).findByCpf(cliente.getCpf());
        // Verificar se o método save nunca foi chamado, pois a exceção foi lançada antes de salvar
        verify(clienteRepositorio, never()).save(any(Cliente.class));
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
    void deveAtualizarClienteComSucesso() {
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(1);
        clienteExistente.setNome("João Silva");
        clienteExistente.setEmail("joao.silva@example.com");
        clienteExistente.setCpf("111.222.333-44");
        clienteExistente.setTelefone("(21) 91234-5678");
        clienteExistente.setDataNascimento(LocalDate.of(1990, 5, 15));

        Cliente clienteAtualizado = new Cliente();
        clienteAtualizado.setNome("João Souza");
        clienteAtualizado.setEmail("joao.souza@example.com");
        clienteAtualizado.setCpf("111.222.333-44");
        clienteAtualizado.setTelefone("(21) 98765-4321");
        clienteAtualizado.setDataNascimento(LocalDate.of(1990, 5, 15));

        when(clienteRepositorio.findById(1)).thenReturn(Optional.of(clienteExistente));

        when(clienteRepositorio.save(any(Cliente.class))).thenReturn(clienteExistente);

        Cliente clienteSalvo = clienteServico.atualizarCliente(1, clienteAtualizado);

        assertNotNull(clienteSalvo);
        assertEquals("João Souza", clienteSalvo.getNome());
        assertEquals("joao.souza@example.com", clienteSalvo.getEmail());
        assertEquals("111.222.333-44", clienteSalvo.getCpf());
        assertEquals("(21) 98765-4321", clienteSalvo.getTelefone());

        verify(clienteRepositorio).save(clienteExistente);
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExistir() {
        when(clienteRepositorio.findById(1)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clienteServico.atualizarCliente(1, new Cliente());
        });

        assertEquals("Cliente com ID 1 não encontrado.", exception.getMessage());
    }
    
    @Test
    void deveAdicionarEnderecoComSucesso() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("João Silva");
        cliente.setEnderecos(new ArrayList<>());

        Endereco novoEndereco = new Endereco();
        novoEndereco.setRua("Rua Nova");
        novoEndereco.setNumero("100");
        novoEndereco.setBairro("Centro");
        novoEndereco.setCidade("Rio de Janeiro");
        novoEndereco.setEstado("RJ");
        novoEndereco.setCep("20000-000");

        when(clienteRepositorio.findById(1)).thenReturn(Optional.of(cliente));

        when(clienteRepositorio.save(any(Cliente.class))).thenReturn(cliente);

        Cliente clienteAtualizado = clienteServico.adicionarEndereco(1, novoEndereco);

        assertNotNull(clienteAtualizado);
        assertEquals(1, clienteAtualizado.getEnderecos().size());
        assertEquals("Rua Nova", clienteAtualizado.getEnderecos().get(0).getRua());

        verify(clienteRepositorio).save(cliente);
    }

    @Test
    void deveAtualizarEnderecoComSucesso() {
        Cliente cliente = new Cliente();
        cliente.setId(1);

        Endereco enderecoExistente = new Endereco();
        enderecoExistente.setId(100);
        enderecoExistente.setRua("Rua Antiga");
        enderecoExistente.setNumero("50");
        enderecoExistente.setBairro("Centro");
        enderecoExistente.setCidade("São Paulo");
        enderecoExistente.setEstado("SP");
        enderecoExistente.setCep("12345-678");

        cliente.setEnderecos(new ArrayList<>());
        cliente.getEnderecos().add(enderecoExistente);

        Endereco enderecoAtualizado = new Endereco();
        enderecoAtualizado.setRua("Rua Atualizada");
        enderecoAtualizado.setNumero("123");
        enderecoAtualizado.setBairro("Novo Bairro");
        enderecoAtualizado.setCidade("Rio de Janeiro");
        enderecoAtualizado.setEstado("RJ");
        enderecoAtualizado.setCep("98765-432");

        when(clienteRepositorio.findById(1)).thenReturn(Optional.of(cliente));

        when(clienteRepositorio.save(any(Cliente.class))).thenReturn(cliente);

        Cliente clienteAtualizado = clienteServico.atualizarEndereco(1, 100, enderecoAtualizado);

        Endereco enderecoSalvo = clienteAtualizado.getEnderecos().get(0);
        assertEquals("Rua Atualizada", enderecoSalvo.getRua());
        assertEquals("123", enderecoSalvo.getNumero());
        assertEquals("Novo Bairro", enderecoSalvo.getBairro());
        assertEquals("Rio de Janeiro", enderecoSalvo.getCidade());
        assertEquals("RJ", enderecoSalvo.getEstado());
        assertEquals("98765-432", enderecoSalvo.getCep());

        verify(clienteRepositorio).save(cliente);
    }

    @Test
    void deveRemoverEnderecoComSucesso() {
        Cliente cliente = new Cliente();
        cliente.setId(1);

        Endereco enderecoExistente = new Endereco();
        enderecoExistente.setId(100);
        enderecoExistente.setRua("Rua Antiga");
        cliente.setEnderecos(new ArrayList<>());
        cliente.getEnderecos().add(enderecoExistente);

        when(clienteRepositorio.findById(1)).thenReturn(Optional.of(cliente));

        when(clienteRepositorio.save(any(Cliente.class))).thenReturn(cliente);

        Cliente clienteAtualizado = clienteServico.removerEndereco(1, 100);

        assertTrue(clienteAtualizado.getEnderecos().isEmpty());

        verify(clienteRepositorio).save(cliente);
    }

    @Test
    void deveBuscarClienteComSeusEnderecos() {
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("João Silva");

        Endereco endereco1 = new Endereco();
        endereco1.setId(100);
        endereco1.setRua("Rua A");
        endereco1.setNumero("123");
        endereco1.setBairro("Bairro A");
        endereco1.setCidade("Cidade A");
        endereco1.setEstado("SP");
        endereco1.setCep("12345-678");

        Endereco endereco2 = new Endereco();
        endereco2.setId(101);
        endereco2.setRua("Rua B");
        endereco2.setNumero("456");
        endereco2.setBairro("Bairro B");
        endereco2.setCidade("Cidade B");
        endereco2.setEstado("RJ");
        endereco2.setCep("98765-432");

        List<Endereco> enderecos = new ArrayList<>();
        enderecos.add(endereco1);
        enderecos.add(endereco2);

        cliente.setEnderecos(enderecos);

        when(clienteRepositorio.findById(1)).thenReturn(Optional.of(cliente));

        Cliente clienteComEnderecos = clienteServico.buscarClienteComEnderecos(1);

        assertNotNull(clienteComEnderecos);
        assertEquals("João Silva", clienteComEnderecos.getNome());
        assertEquals(2, clienteComEnderecos.getEnderecos().size());

        Endereco enderecoSalvo1 = clienteComEnderecos.getEnderecos().get(0);
        Endereco enderecoSalvo2 = clienteComEnderecos.getEnderecos().get(1);

        assertEquals("Rua A", enderecoSalvo1.getRua());
        assertEquals("123", enderecoSalvo1.getNumero());
        assertEquals("Bairro A", enderecoSalvo1.getBairro());
        assertEquals("SP", enderecoSalvo1.getEstado());
        assertEquals("12345-678", enderecoSalvo1.getCep());

        assertEquals("Rua B", enderecoSalvo2.getRua());
        assertEquals("456", enderecoSalvo2.getNumero());
        assertEquals("Bairro B", enderecoSalvo2.getBairro());
        assertEquals("RJ", enderecoSalvo2.getEstado());
        assertEquals("98765-432", enderecoSalvo2.getCep());

        verify(clienteRepositorio).findById(1);
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

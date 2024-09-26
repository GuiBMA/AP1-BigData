package br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Repositorio.ClienteRepositorio;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Repositorio.EnderecoRepositorio;
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
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class Ap1GerenciamentoCliTests {
    @Mock
    private ClienteRepositorio clienteRepositorio;

    @Mock
    private EnderecoRepositorio enderecoRepositorio;

    @InjectMocks
    private ClienteServico clienteServico;

    private Validator validator;

    public Cliente criarClienteValido() {
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao.silva@email.com");
        cliente.setCpf("123.456.789-10");
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));  // Cliente com mais de 18 anos
        cliente.setTelefone("(21) 91234-5678");
    
        // Simula o salvamento do cliente no repositório
        when(clienteRepositorio.save(cliente)).thenReturn(cliente);
    
        return cliente;
    }
    
    @BeforeEach
    public void setUp() {
    
        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

// Testes Cliente
    @Test
    public void deveFalharSeNomeForMenorQue3Caracteres() {
        Cliente cliente = new Cliente();
        cliente.setNome("Al");
        cliente.setEmail("al.silva@email.com");
        cliente.setCpf("123.456.789-10");
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));
        cliente.setTelefone("(21) 91234-5678");
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O nome deve ter entre 3 e 100 caracteres")),
                "Deve falhar pois o nome tem menos de 3 caracteres");
        verify(clienteRepositorio, never()).save(any(Cliente.class));
    }
    @Test
    public void deveCriarClienteValido() {
        Cliente cliente = new Cliente();
        cliente.setNome("João Silva");
        cliente.setEmail("joao.silva@email.com");
        cliente.setCpf("123.456.789-10");
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));
        cliente.setTelefone("(21) 91234-5678");

        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
    
        assertTrue(violations.isEmpty(), "Cliente deve ser válido");
        when(clienteRepositorio.save(any(Cliente.class))).thenReturn(cliente);
        clienteServico.salvar(cliente);
        verify(clienteRepositorio, times(1)).save(cliente);
    }
    @Test
    public void deveFalharSeNomeForInvalido() {
        Cliente cliente = new Cliente();
        cliente.setNome("");
        cliente.setEmail("maria.silva@email.com");
        cliente.setCpf("123.456.789-10");
        cliente.setDataNascimento(LocalDate.of(1995, 1, 1));
        cliente.setTelefone("(21) 91234-5678");
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Nome é obrigatório")),
                "Deve falhar pois o nome está em branco");
        verify(clienteRepositorio, never()).save(any(Cliente.class));
    }
    @Test
    public void deveFalharQuandoEmailClienteForInvalido() {
        Cliente cliente = new Cliente();
        cliente.setNome("Teste email");
        cliente.setEmail("email_invalido");
        cliente.setCpf("123.456.789-10");
        cliente.setDataNascimento(LocalDate.of(2020, 1, 1));
        cliente.setTelefone("12345-6789");

        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertTrue(!violations.isEmpty(), "Cliente inválido deve ter violações");
        verify(clienteRepositorio, never()).save(any(Cliente.class));
    }
    @Test
    public void deveFalharSeEmailEstiverEmBranco() {
        Cliente cliente = new Cliente();
        cliente.setNome("Maria Silva");
        cliente.setEmail("");
        cliente.setCpf("123.456.789-10");
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));
        cliente.setTelefone("(21) 91234-5678");
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Email é obrigatório")),
                "Deve falhar pois o email está em branco");
        verify(clienteRepositorio, never()).save(any(Cliente.class));
    }
    @Test
    public void deveFalharSeCpfForInvalido() {
        Cliente cliente = new Cliente();
        cliente.setNome("Roberto Silva");
        cliente.setEmail("roberto.silva@email.com");
        cliente.setCpf("123.456.789-999");
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));
        cliente.setTelefone("(21) 91234-5678");
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("CPF deve seguir o formato XXX.XXX.XXX-XX")),
                "Deve falhar pois o CPF não segue o formato XXX.XXX.XXX-XX");
        verify(clienteRepositorio, never()).save(any(Cliente.class));
    }
    @Test
    public void deveFalharSeCpfEstiverEmBranco() {
        // Cliente com CPF em branco
        Cliente cliente = new Cliente();
        cliente.setNome("João Pereira");
        cliente.setEmail("joao.pereira@email.com");
        cliente.setCpf("");  // CPF em branco
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));  // Cliente maior de idade
        cliente.setTelefone("(21) 91234-5678");

        // Validação
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);

        // Verifica se a violação está relacionada ao CPF em branco
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("CPF é obrigatório")),
                "Deve falhar pois o CPF está em branco");

        // Verifica se o repositório não foi chamado para salvar o cliente
        verify(clienteRepositorio, never()).save(any(Cliente.class));
    }
    @Test
    public void deveFalharSeDataNascimentoEstiverNoFuturo() {
        Cliente cliente = new Cliente();
        cliente.setNome("Carlos Alberto");
        cliente.setEmail("carlos.alberto@email.com");
        cliente.setCpf("123.456.789-10");
        cliente.setDataNascimento(LocalDate.of(2050, 1, 1));
        cliente.setTelefone("(21) 91234-5678");
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Data de nascimento deve ser válida")),
                "Deve falhar pois a data de nascimento está no futuro");
        verify(clienteRepositorio, never()).save(any(Cliente.class));
    }
    @Test
    public void deveFalharSeClienteForMenorDe18Anos() {
        Cliente cliente = new Cliente();
        cliente.setNome("Pedro Júnior");
        cliente.setEmail("pedro.junior@email.com");
        cliente.setCpf("123.456.789-10");
        cliente.setDataNascimento(LocalDate.of(2010, 1, 1));
        cliente.setTelefone("(21) 91234-5678");
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);

        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Cliente deve ter mais de 18 anos")),
                "Deve falhar pois o cliente é menor de 18 anos");

        verify(clienteRepositorio, never()).save(any(Cliente.class));
    }
    @Test
    public void deveFalharSeTelefoneForInvalido() {
        Cliente cliente = new Cliente();
        cliente.setNome("Ana Maria");
        cliente.setEmail("ana.maria@email.com");
        cliente.setCpf("123.456.789-10");
        cliente.setDataNascimento(LocalDate.of(1990, 1, 1));
        cliente.setTelefone("12345-6789");
        Set<ConstraintViolation<Cliente>> violations = validator.validate(cliente);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O telefone deve seguir o padrão (XX) XXXXX-XXXX")),
                "Deve falhar pois o telefone não segue o formato (XX) XXXXX-XXXX");
        verify(clienteRepositorio, never()).save(any(Cliente.class));
    }

// Testes Endereco
    @Test
    public void deveFalharSeCepForInvalido() {
        Cliente cliente = criarClienteValido();
        Endereco endereco = new Endereco();
        endereco.setRua("Rua Principal");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCep("1234567");
        endereco.setCliente(cliente);
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("CEP deve seguir o formato XXXXX-XXX")),
                "Deve falhar pois o CEP não segue o formato XXXXX-XXX");

        verify(enderecoRepositorio, never()).save(any(Endereco.class));
    }
    @Test
    public void deveFalharSeRuaEstiverEmBranco() {
        Cliente cliente = criarClienteValido();
        Endereco endereco = new Endereco();
        endereco.setRua("");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCep("12345-678");
        endereco.setCliente(cliente);
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Rua é obrigatória")),
                "Deve falhar pois a rua está em branco");
        verify(enderecoRepositorio, never()).save(any(Endereco.class));
    }
    @Test
    public void deveFalharSeNumeroEstiverEmBranco() {
        Cliente cliente = criarClienteValido();
        Endereco endereco = new Endereco();
        endereco.setRua("Rua Principal");
        endereco.setNumero("");
        endereco.setBairro("Centro");
        endereco.setCep("12345-678");
        endereco.setCliente(cliente);
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Número é obrigatório")),
                "Deve falhar pois o número está em branco");
        verify(enderecoRepositorio, never()).save(any(Endereco.class));
    }
    @Test
    public void deveFalharSeBairroEstiverEmBranco() {
        Cliente cliente = criarClienteValido();
        Endereco endereco = new Endereco();
        endereco.setRua("Rua Principal");
        endereco.setNumero("123");
        endereco.setBairro("");
        endereco.setCep("12345-678");
        endereco.setCliente(cliente);
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Bairro é obrigatório")),
                "Deve falhar pois o bairro está em branco");
        verify(enderecoRepositorio, never()).save(any(Endereco.class));
    }
    @Test
    public void deveFalharSeEstadoForInvalido() {
        Cliente cliente = criarClienteValido();
        Endereco endereco = new Endereco();
        endereco.setEstado("XY");
        endereco.setCidade("Rio de Janeiro");
        endereco.setBairro("Centro");
        endereco.setRua("Rua Principal");
        endereco.setCep("12345-678");
        endereco.setCliente(cliente);
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Estado inválido")),
                "Deve falhar pois o estado é inválido");
        verify(enderecoRepositorio, never()).save(any(Endereco.class));
    }
    @Test
    public void deveFalharSeEstadoEstiverEmBranco() {
        Cliente cliente = criarClienteValido();
        Endereco endereco = new Endereco();
        endereco.setEstado("");
        endereco.setCidade("Rio de Janeiro");
        endereco.setBairro("Centro");
        endereco.setRua("Rua Principal");
        endereco.setCep("12345-678");
        endereco.setCliente(cliente);
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Estado é obrigatório")),
                "Deve falhar pois o estado está em branco");
        verify(enderecoRepositorio, never()).save(any(Endereco.class));
    }
    @Test
    public void deveFalharSeCidadeForMenorQue2Caracteres() {
        Cliente cliente = criarClienteValido();
        Endereco endereco = new Endereco();
        endereco.setEstado("RJ");
        endereco.setCidade("A");
        endereco.setBairro("Centro");
        endereco.setRua("Rua Principal");
        endereco.setCep("12345-678");
        endereco.setCliente(cliente);
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("A cidade deve ter entre 2 e 100 caracteres")),
                "Deve falhar pois a cidade tem menos de 2 caracteres");
        verify(enderecoRepositorio, never()).save(any(Endereco.class));
    }
    @Test
    public void deveFalharSeCidadeEstiverEmBranco() {
        Cliente cliente = criarClienteValido();
        Endereco endereco = new Endereco();
        endereco.setEstado("RJ");
        endereco.setCidade("");
        endereco.setBairro("Centro");
        endereco.setRua("Rua Principal");
        endereco.setCep("12345-678");
        endereco.setCliente(cliente);
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Cidade é obrigatória")),
                "Deve falhar pois a cidade está em branco");
        verify(enderecoRepositorio, never()).save(any(Endereco.class));
    }
    @Test
    public void deveFalharSeBairroForMenorQue3Caracteres() {
        Cliente cliente = criarClienteValido();
        Endereco endereco = new Endereco();
        endereco.setEstado("RJ");
        endereco.setCidade("Rio de Janeiro");
        endereco.setBairro("AB");
        endereco.setRua("Rua Principal");
        endereco.setCep("12345-678");
        endereco.setCliente(cliente);
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("O bairro deve ter entre 3 e 100 caracteres")),
                "Deve falhar pois o bairro tem menos de 3 caracteres");
        verify(enderecoRepositorio, never()).save(any(Endereco.class));
    }
    @Test
    public void deveFalharSeRuaForMenorQue3Caracteres() {
        Cliente cliente = criarClienteValido();
        Endereco endereco = new Endereco();
        endereco.setEstado("RJ");
        endereco.setCidade("Rio de Janeiro");
        endereco.setBairro("Centro");
        endereco.setRua("AB");
        endereco.setCep("12345-678");
        endereco.setCliente(cliente);
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("A rua deve ter entre 3 e 255 caracteres")),
                "Deve falhar pois a rua tem menos de 3 caracteres");
        verify(enderecoRepositorio, never()).save(any(Endereco.class));
    }
    @Test
    public void deveFalharSeCepEstiverEmBranco() {
        Cliente cliente = criarClienteValido();
        Endereco endereco = new Endereco();
        endereco.setEstado("RJ");
        endereco.setCidade("Rio de Janeiro");
        endereco.setBairro("Centro");
        endereco.setRua("Rua Principal");
        endereco.setCep("");
        endereco.setCliente(cliente);
        Set<ConstraintViolation<Endereco>> violations = validator.validate(endereco);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("CEP é obrigatório")),
                "Deve falhar pois o CEP está em branco");
        verify(enderecoRepositorio, never()).save(any(Endereco.class));
    }

}

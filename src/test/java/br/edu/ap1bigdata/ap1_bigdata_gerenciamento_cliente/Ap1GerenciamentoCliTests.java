package br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente;

import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Repositorio.ClienteRepositorio;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Servico.ClienteServico;
import br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model.Cliente;
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

    @InjectMocks
    private ClienteServico clienteServico;

    private Validator validator;

    @BeforeEach
    public void setUp() {
    
        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

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

}

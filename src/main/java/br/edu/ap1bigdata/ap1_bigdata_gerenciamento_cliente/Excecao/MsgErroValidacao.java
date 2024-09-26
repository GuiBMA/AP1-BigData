package br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Excecao;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class MsgErroValidacao {
    private String message = "Há erros na sua requisição, verique";
    private List<ErroValidacao> errors = new ArrayList<>();
}

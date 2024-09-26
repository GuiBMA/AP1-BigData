package br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Excecao;
import lombok.Data;

@Data
public class ErroValidacao {
    private String field;
    private String message;
}

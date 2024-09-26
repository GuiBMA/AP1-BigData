package br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.Excecao;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class InterceptadorErroValidacao {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    
    public MsgErroValidacao validationErrorHandler(MethodArgumentNotValidException i) {
        MsgErroValidacao response = new MsgErroValidacao();

        for (FieldError item : i.getFieldErrors()) {
            ErroValidacao error = new ErroValidacao();
            error.setField(item.getField());
            error.setMessage(item.getDefaultMessage());
            response.getErrors().add(error);
        }

        return response;
    }
}

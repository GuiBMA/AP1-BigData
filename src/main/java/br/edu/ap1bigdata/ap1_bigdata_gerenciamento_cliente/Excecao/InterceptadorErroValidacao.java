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

    @ExceptionHandler({MethodArgumentNotValidException.class, IllegalArgumentException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MsgErroValidacao handleExceptions(Exception ex) {
        MsgErroValidacao response = new MsgErroValidacao();

        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validationException = (MethodArgumentNotValidException) ex;
            for (FieldError item : validationException.getFieldErrors()) {
                ErroValidacao error = new ErroValidacao();
                error.setField(item.getField());
                error.setMessage(item.getDefaultMessage());
                response.getErrors().add(error);
            }
        } else if (ex instanceof IllegalArgumentException) {
            ErroValidacao error = new ErroValidacao();
            error.setMessage(ex.getMessage());
            response.getErrors().add(error);
        }

        return response;
    }
}


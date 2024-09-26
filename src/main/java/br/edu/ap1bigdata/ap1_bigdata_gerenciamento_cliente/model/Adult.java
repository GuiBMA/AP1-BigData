package br.edu.ap1bigdata.ap1_bigdata_gerenciamento_cliente.model;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AdultValidator.class)
public @interface Adult {
    String message() default "O cliente deve ter mais de 18 anos";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
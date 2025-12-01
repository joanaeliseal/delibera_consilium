package br.edu.ifpb.pweb2.delibera_consilium.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD}) 
@Retention(RetentionPolicy.RUNTIME) 
@Constraint(validatedBy = MatriculaValidator.class) 
public @interface Matricula {

    String message() default "Matrícula inválida. Deve conter apenas números.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

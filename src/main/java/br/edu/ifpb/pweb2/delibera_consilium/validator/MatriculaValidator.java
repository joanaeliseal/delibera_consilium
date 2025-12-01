package br.edu.ifpb.pweb2.delibera_consilium.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatriculaValidator implements ConstraintValidator<Matricula, String> {

    @Override
    public void initialize(Matricula constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; 
        }

        //Verifica se contém apenas dígitos (0-9) e tem tamanho maior ou igual a 8
        return value.matches("[0-9]+") && value.length() >= 8;
    }
}
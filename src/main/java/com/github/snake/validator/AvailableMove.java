package com.github.snake.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AvailableMoveValidator.class)
public @interface AvailableMove {

  String message() default "Not available move.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

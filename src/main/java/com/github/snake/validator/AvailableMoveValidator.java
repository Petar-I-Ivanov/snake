package com.github.snake.validator;

import com.github.snake.utilities.Constants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AvailableMoveValidator implements ConstraintValidator<AvailableMove, String> {

  @Override
  public boolean isValid(String action, ConstraintValidatorContext context) {

    if (action.length() != 1) {
      return false;
    }

    return Constants.isCharAvailableMove(action.charAt(0));
  }
}

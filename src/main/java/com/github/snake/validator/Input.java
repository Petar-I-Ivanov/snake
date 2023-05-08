package com.github.snake.validator;

import lombok.Data;

@Data
public class Input {

  @AvailableMove
  private String action;
}

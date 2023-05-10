package com.github.snake.models.gameboard.enemy;

import com.github.snake.utilities.Constants;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Poacher extends Enemy {

  public Poacher() {
    super(Constants.POACHER_SIGN);
  }
}

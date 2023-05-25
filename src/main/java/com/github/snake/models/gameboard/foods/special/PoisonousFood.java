package com.github.snake.models.gameboard.foods.special;

import com.github.snake.utilities.Constants;
import jakarta.persistence.Entity;

@Entity
public class PoisonousFood extends SpecialFood {

  public PoisonousFood() {
    super(Constants.POISONOUS_FOOD_SIGN);
  }
}

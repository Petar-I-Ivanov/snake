package com.github.snake.models.gameboard.foods.special;

import com.github.snake.utilities.Constants;
import jakarta.persistence.Entity;

@Entity
public class BorderFood extends SpecialFood {

  public BorderFood() {
    super(Constants.SPECIAL_BORDER_FOOD_SIGN);
  }
}

package com.github.snake.models.gameboard.foods.special;

import com.github.snake.utilities.Constants;
import jakarta.persistence.Entity;

@Entity
public class GrowthFood extends SpecialFood {

  public GrowthFood() {
    super(Constants.SPECIAL_GROWTH_FOOD_SIGN);
  }
}

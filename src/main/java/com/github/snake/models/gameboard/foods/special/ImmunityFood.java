package com.github.snake.models.gameboard.foods.special;

import com.github.snake.utilities.Constants;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class ImmunityFood extends SpecialFood {

  public ImmunityFood() {
    super(Constants.SPECIAL_IMMUNITY_FOOD_SIGN);
  }
}

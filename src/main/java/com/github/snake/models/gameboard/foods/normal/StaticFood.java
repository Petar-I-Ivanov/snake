package com.github.snake.models.gameboard.foods.normal;

import com.github.snake.utilities.Constants;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class StaticFood extends NormalFood {

  public StaticFood() {
    super(Constants.NORMAL_STATIC_FOOD_SIGN);
  }
}

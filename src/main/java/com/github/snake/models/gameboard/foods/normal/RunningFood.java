package com.github.snake.models.gameboard.foods.normal;

import com.github.snake.utilities.Constants;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class RunningFood extends NormalFood {

  public RunningFood() {
    super(Constants.NORMAL_RUNNING_FOOD_SIGN);
  }
}

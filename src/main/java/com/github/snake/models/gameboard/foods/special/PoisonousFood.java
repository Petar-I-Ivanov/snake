package com.github.snake.models.gameboard.foods.special;

import com.github.snake.models.Game;
import com.github.snake.utilities.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class PoisonousFood extends SpecialFood {

  @ManyToOne
  @JoinColumn(name = "game_id")
  private Game game;

  public PoisonousFood() {
    super(Constants.POISONOUS_FOOD_SIGN);
  }
}

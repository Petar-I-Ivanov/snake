package com.github.snake.models.gameboard.foods;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.GameboardObject;
import com.github.snake.utilities.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class PoisonousFood extends GameboardObject {

  @ManyToOne
  @JoinColumn(name = "game_id")
  private Game game;

  public PoisonousFood() {
    super(Constants.POISONOUS_FOOD_SIGN);
  }
}

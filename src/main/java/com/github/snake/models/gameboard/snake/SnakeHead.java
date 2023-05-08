package com.github.snake.models.gameboard.snake;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.GameboardObject;
import com.github.snake.utilities.Constants;
import com.github.snake.utilities.Position;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class SnakeHead extends GameboardObject {

  private static final byte STARTING_ROW_COL = 2;

  @OneToOne
  @JoinColumn(name = "game_id")
  private Game game;

  public SnakeHead() {
    super(Constants.SNAKE_HEAD_SIGN, new Position(STARTING_ROW_COL, STARTING_ROW_COL));
  }
}

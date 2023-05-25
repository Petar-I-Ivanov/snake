package com.github.snake.models.gameboard.snake;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.GameboardObject;
import com.github.snake.utilities.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "snake_body")
public class SnakeBody extends GameboardObject {

  @ManyToOne
  @JoinColumn(name = "game_id")
  private Game game;

  public SnakeBody() {
    super(Constants.SNAKE_BODY_SIGN);
  }
}

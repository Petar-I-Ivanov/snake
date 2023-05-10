package com.github.snake.models.gameboard.enemy;

import com.github.snake.models.Game;
import com.github.snake.utilities.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Trap extends Enemy {

  @ManyToOne
  @JoinColumn(name = "game_id")
  private Game game;

  public Trap() {
    super(Constants.TRAP_SIGN);
  }
}

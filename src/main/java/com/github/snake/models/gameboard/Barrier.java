package com.github.snake.models.gameboard;

import com.github.snake.models.Game;
import com.github.snake.utilities.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Barrier extends GameboardObject {

  @ManyToOne
  @JoinColumn(name = "game_id")
  private Game game;

  public Barrier() {
    super(Constants.BARRIER_SIGN);
  }
}

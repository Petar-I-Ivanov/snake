package com.github.snake.models.gameboard;

import com.github.snake.models.Game;
import com.github.snake.utilities.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "game_exit")
public class Exit extends GameboardObject {

  @OneToOne
  @JoinColumn(name = "game_id")
  private Game game;

  public Exit() {
    super(Constants.EXIT_SIGN);
  }
}

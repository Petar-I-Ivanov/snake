package com.github.snake.models.gameboard.enemy;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.GameboardObject;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Enemy extends GameboardObject {

  @ManyToOne
  @JoinColumn(name = "game_id")
  private Game game;

  protected Enemy(String sign) {
    super(sign);
  }
}

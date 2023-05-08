package com.github.snake.models.gameboard.foods.special;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.GameboardObject;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@MappedSuperclass
public class SpecialFood extends GameboardObject {

  @ManyToOne
  @JoinColumn(name = "game_id")
  protected Game game;

  protected SpecialFood(String sign) {
    super(sign);
  }
}

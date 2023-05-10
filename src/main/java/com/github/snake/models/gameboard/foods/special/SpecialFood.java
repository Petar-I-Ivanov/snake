package com.github.snake.models.gameboard.foods.special;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.GameboardObject;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "special_food")
public class SpecialFood extends GameboardObject {

  @ManyToOne
  @JoinColumn(name = "game_id")
  protected Game game;

  protected SpecialFood(String sign) {
    super(sign);
  }
}

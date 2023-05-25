package com.github.snake.models.gameboard.foods.normal;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.GameboardObject;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "normal_food")
public class NormalFood extends GameboardObject {

  @OneToOne
  @JoinColumn(name = "game_id")
  private Game game;

  protected NormalFood(String sign) {
    super(sign);
  }
}

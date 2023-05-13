package com.github.snake.models.gameboard.snake;

import com.github.snake.models.Game;
import com.github.snake.models.gameboard.GameboardObject;
import com.github.snake.utilities.Constants;
import com.github.snake.utilities.Position;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "snake_head")
public class SnakeHead extends GameboardObject {

  private static final byte STARTING_ROW_COL = 2;

  @Column(name = "is_border_food_active", nullable = false)
  private boolean isBorderFoodActive;

  @Column(name = "is_growth_food_active", nullable = false)
  private boolean isGrowthFoodActive;

  @Column(name = "is_immunity_food_active", nullable = false)
  private boolean isImmunityFoodActive;

  @Column(name = "is_poisonous_food_active", nullable = false)
  private boolean isPoisonousFoodActive;
  
  @Column(name = "is_killed", nullable = false)
  private boolean isKilled;
  
  @Column(name = "is_escaped", nullable = false)
  private boolean isEscaped;

  @OneToOne
  @JoinColumn(name = "game_id")
  private Game game;

  public SnakeHead() {
    super(Constants.SNAKE_HEAD_SIGN, new Position(STARTING_ROW_COL, STARTING_ROW_COL));
  }
}

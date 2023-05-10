package com.github.snake.models;

import com.github.snake.models.gameboard.Barrier;
import com.github.snake.models.gameboard.Exit;
import com.github.snake.models.gameboard.enemy.Enemy;
import com.github.snake.models.gameboard.foods.normal.NormalFood;
import com.github.snake.models.gameboard.foods.special.SpecialFood;
import com.github.snake.models.gameboard.snake.SnakeBody;
import com.github.snake.models.gameboard.snake.SnakeHead;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.Data;

@Data
@Entity
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private GameStatusEnum status;

  private short turn;

  @OneToMany(mappedBy = "game")
  private List<Barrier> barriers;

  @OneToOne(mappedBy = "game")
  private Exit exit;

  @OneToOne(mappedBy = "game")
  private NormalFood normalFood;

  @OneToMany(mappedBy = "game")
  private List<SpecialFood> specialFoods;

  @OneToMany(mappedBy = "game")
  private List<Enemy> enemies;

  @OneToOne(mappedBy = "game")
  private SnakeHead snakeHead;

  @OneToMany(mappedBy = "game")
  private List<SnakeBody> snakeBodies;

  public Game() {
    this.status = GameStatusEnum.LEVEL_ONE;
    this.turn = 1;
  }
}

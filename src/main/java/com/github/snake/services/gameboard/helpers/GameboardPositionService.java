package com.github.snake.services.gameboard.helpers;

import com.github.snake.models.gameboard.Barrier;
import com.github.snake.models.gameboard.Exit;
import com.github.snake.models.gameboard.enemy.Enemy;
import com.github.snake.models.gameboard.foods.normal.NormalFood;
import com.github.snake.models.gameboard.foods.special.BorderFood;
import com.github.snake.models.gameboard.foods.special.GrowthFood;
import com.github.snake.models.gameboard.foods.special.ImmunityFood;
import com.github.snake.models.gameboard.foods.special.PoisonousFood;
import com.github.snake.models.gameboard.snake.SnakeHead;
import com.github.snake.repositories.Repository;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GameboardPositionService {

  private Repository repository;

  public GameboardPositionService(Repository repository) {
    this.repository = repository;
  }

  public Position getSnakeHeadPosition(Long gameId) {
    return repository.findSingleByGameId(gameId, SnakeHead.class).getLocation();
  }

  public boolean isPositionOccupied(Long gameId, Position position) {
    return repository.findAnyByGameIdAndPosition(gameId, position) != null;
  }

  public boolean isPositionNormalFood(Long gameId, Position position) {
    return repository.findByGameIdAndPosition(gameId, position, NormalFood.class) != null;
  }

  public boolean isPositionBorderFood(Long gameId, Position position) {
    return repository.findByGameIdAndPosition(gameId, position, BorderFood.class) != null;
  }

  public boolean isPositionGrowthFood(Long gameId, Position position) {
    return repository.findByGameIdAndPosition(gameId, position, GrowthFood.class) != null;
  }

  public boolean isPositionImmunityFood(Long gameId, Position position) {
    return repository.findByGameIdAndPosition(gameId, position, ImmunityFood.class) != null;
  }

  public boolean isPositionPoisonousFood(Long gameId, Position position) {
    return repository.findByGameIdAndPosition(gameId, position, PoisonousFood.class) != null;
  }

  public boolean isPositionEnemy(Long gameId, Position position) {
    return repository.findByGameIdAndPosition(gameId, position, Enemy.class) != null;
  }

  public boolean isPositionBarrier(Long gameId, Position position) {
    return repository.findByGameIdAndPosition(gameId, position, Barrier.class) != null;
  }

  public boolean isPositionExit(Long gameId, Position position) {
    return repository.findByGameIdAndPosition(gameId, position, Exit.class) != null;
  }
}

package com.github.snake.services.food;

import com.github.snake.models.Game;
import com.github.snake.services.SnakeService;
import com.github.snake.utilities.Position;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FoodService {

  private SnakeService snakeService;

  private NormalFoodService normalFoodService;
  private SpecialFoodService specialFoodService;

  public FoodService(NormalFoodService normalFoodService, SpecialFoodService specialFoodService,
      SnakeService snakeService) {

    this.snakeService = snakeService;

    this.normalFoodService = normalFoodService;
    this.specialFoodService = specialFoodService;
  }

  public boolean isPositionNormalFood(Long gameId, Position position) {
    return normalFoodService.isPositionNormalFood(gameId, position);
  }

  public boolean isPositionPoisonousFood(Long gameId, Position position) {
    return specialFoodService.isPositionPoisonousFood(gameId, position);
  }

  public boolean isPositionBorderFood(Long gameId, Position position) {
    return specialFoodService.isPositionBorderFood(gameId, position);
  }

  public boolean isPositionGrowthFood(Long gameId, Position position) {
    return specialFoodService.isPositionGrowthFood(gameId, position);
  }

  public boolean isPositionImmunityFood(Long gameId, Position position) {
    return specialFoodService.isPositionImmunityFood(gameId, position);
  }

  public void foodCheck(Game game) {
    normalFoodService.normalFoodCheck(game, snakeService.getSnakeHeadLocation(game.getId()));
    specialFoodService.specialFoodCheck(game, snakeService.getSnakeHeadLocation(game.getId()));
  }

  public void eatFoodAtPosition(Long gameId, Position position) {
    normalFoodService.eatFoodAtPosition(gameId, position);
    specialFoodService.eatFoodAtPosition(gameId, position);
  }
}
